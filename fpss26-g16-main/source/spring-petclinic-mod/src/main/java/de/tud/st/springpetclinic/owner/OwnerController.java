/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tud.st.springpetclinic.owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.tud.st.springpetclinic.medication.services.Contract;
import de.tud.st.springpetclinic.medication.services.ContractHandler;
import de.tud.st.springpetclinic.medication.services.ContractRepository;
import de.tud.st.springpetclinic.owner.owner.Owner;
import de.tud.st.springpetclinic.owner.owner.OwnerRepository;
import de.tud.st.springpetclinic.repository.VetRepository;
import de.tud.st.springpetclinic.system.storage.Storage;
import de.tud.st.springpetclinic.system.storage.UsedItem;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import de.tud.st.springpetclinic.system.storage.Item;
import de.tud.st.springpetclinic.system.storage.UsedItem;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Karl Kegel
 */
@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private static final String VIEWS_CONTRACT_CREATE_FORM = "contracts/createContractForm";

	private final OwnerRepository owners;

	private final ContractRepository contractRepository;

	private final ContractHandler contractHandler;

	private final Storage storage;

	private final VetRepository vetRepository;

	public OwnerController(OwnerRepository clinicService, ContractRepository contractRepository,
			ContractHandler contractHandler, Storage storage, VetRepository vetRepository) {
		this.owners = clinicService;
		this.contractRepository = contractRepository;
		this.contractHandler = contractHandler;
		this.storage = storage;
		this.vetRepository = vetRepository;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		return ownerId == null ? new Owner() : this.owners.findById(ownerId);
	}

	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		this.owners.save(owner);
		return "redirect:/owners/" + owner.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model) {
		if (owner.getLastName() == null) {
			owner.setLastName("");
		}
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
		if (ownersResults.isEmpty()) {
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}
		if (ownersResults.getTotalElements() == 1) {
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}
		return addPaginationModel(page, model, ownersResults);
	}

	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		model.addAttribute("listOwners", paginated);
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.owners.findById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		owner.setId(ownerId);
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner owner = this.owners.findById(ownerId);
		mav.addObject(owner);
		return mav;
	}

	@GetMapping("/owners/{ownerId}/contracts/new")
	public String initContractCreationForm(@PathVariable("ownerId") int ownerId, Map<String, Object> model) {
		Contract contract = new Contract("", 0.0, "", new ArrayList<>());
		model.put("contract", contract);
		model.put("storage", storage);
		model.put("ownerId", ownerId);
		model.put("vets", vetRepository.findAll());
		return VIEWS_CONTRACT_CREATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/contracts")
public String processContractCreationForm(
        @PathVariable("ownerId") int ownerId,
        @Valid @ModelAttribute Contract contract,
        BindingResult result,
        Model model,
        @RequestParam(required = false) Integer vetId,
        @RequestParam(required = false) List<Integer> itemIds) {

    if (result.hasErrors()) {
        model.addAttribute("storage", storage);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("vets", vetRepository.findAll());
        return VIEWS_CONTRACT_CREATE_FORM;
    }

    // =========================
    // 🔥 FIX 1: HANDLE ITEMS
    // =========================
    List<UsedItem> usedItems = new ArrayList<>();

    if (itemIds != null) {
    for (Integer id : itemIds) {

        storage.findById(id).ifPresent(item -> {
            UsedItem used = new UsedItem(item.getDescription(), item.getPrice());
            usedItems.add(used);
        });

    }
}

    contract.setUsedItems(usedItems);

    // =========================
    // 🔥 FIX 2: HANDLE VET
    // =========================
    if (vetId != null) {
        for (de.tud.st.springpetclinic.model.Vet v : vetRepository.findAll()) {
            if (v.getId() == vetId) {
                contract.setVet(v);
                break;
            }
        }
    }

    // =========================
    // 🔥 FIX 3: SAVE CONTRACT
    // =========================
    Owner owner = owners.findById(ownerId);

    Contract startedContract = contractHandler.startContract(owner, contract);
    contractRepository.save(startedContract);

    return "redirect:/owners/" + ownerId;
}

}