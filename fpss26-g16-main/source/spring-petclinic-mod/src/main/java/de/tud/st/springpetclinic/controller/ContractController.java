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

package de.tud.st.springpetclinic.controller;

import de.tud.st.springpetclinic.medication.services.ContractHandler;
import de.tud.st.springpetclinic.medication.services.ContractRepository;
import de.tud.st.springpetclinic.owner.owner.Owner;
import de.tud.st.springpetclinic.owner.owner.OwnerRepository;
import de.tud.st.springpetclinic.system.storage.Storage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Karl Kegel
 */
@Controller
public class ContractController {

	// autowired components, you can expect them to be available at runtime:
	private final ContractRepository contractRepository;

	private final ContractHandler contractHandler;

	private final Storage storage;

	private final OwnerRepository ownerRepository;

	// Constructor is used for injection
	public ContractController(ContractRepository contractRepository, ContractHandler contractHandler, Storage storage,
			OwnerRepository ownerRepository) {
		this.contractRepository = contractRepository;
		this.contractHandler = contractHandler;
		this.storage = storage;
		this.ownerRepository = ownerRepository;
	}

	@GetMapping("/contracts.html")
	public String showContracts(Model model) {

		var owners = ownerRepository.findAllNotPaged();
		Map<Long, Double> contractTotals = new HashMap<>();
		for (var owner : owners) {
			if (owner.bookedServices != null) {
				for (var contract : owner.bookedServices) {
					contractTotals.put(contract.getId(), contractHandler.calculateCurrentPriceSum(contract));
				}
			}
		}
		model.addAttribute("listContracts", owners);
		model.addAttribute("contractTotals", contractTotals);

		return "contracts/contractsList";
	}

	@GetMapping("/contracts/details.html")
	public String showContractDetails(@RequestParam int contractId, Model model) {

		var contractOpt = contractRepository.findById(contractId);
		if (contractOpt.isPresent()) {
			model.addAttribute("contract", contractOpt.get());
		}
		return "contracts/contractDetails";
	}

}
