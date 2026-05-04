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

package de.tud.st.springpetclinic.medication.services;


import de.tud.st.springpetclinic.owner.owner.Owner;
import de.tud.st.springpetclinic.system.storage.Item;
import org.springframework.stereotype.Service;

/**
 * @author Karl Kegel
 */
@Service
public class ContractHandler {

    public Contract startContract(Owner owner, Contract contract){
        //TODO
        return null;
    }

    public Contract useItem(Item item, Contract contract){
        //prints failure if item not available
        //TODO
        return null;
    }

    public double calculateCurrentPriceSum(Contract contract){
        //TODO
        return 0.00;
    }

}
