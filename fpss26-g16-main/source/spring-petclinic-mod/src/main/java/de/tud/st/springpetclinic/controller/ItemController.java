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
import de.tud.st.springpetclinic.owner.owner.OwnerRepository;
import de.tud.st.springpetclinic.system.storage.Storage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Karl Kegel
 */
@Controller
public class ItemController {

	//autowired components, you can expect them to be available at runtime:
	private final ContractRepository contractRepository;
	private final ContractHandler contractHandler;
	private final Storage storage;


	//Constructor is used for injection
	public ItemController(ContractRepository contractRepository,
                          ContractHandler contractHandler,
                          Storage storage) {
		this.contractRepository = contractRepository;
		this.contractHandler = contractHandler;
		this.storage = storage;
	}

	/*
	 * TODO Task 1.d Add an item to a contract. Use ContractHandler.useItem(...)
	 */

}
