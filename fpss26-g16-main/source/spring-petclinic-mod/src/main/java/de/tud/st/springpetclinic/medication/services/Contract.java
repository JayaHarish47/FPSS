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

import de.tud.st.springpetclinic.system.storage.UsedItem;
import jakarta.persistence.*;

import java.util.List;

/**
 * @author Karl Kegel
 */
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;

    private double basePrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private List<UsedItem> usedItems;

    public Contract(String description, double basePrice, String name, List<UsedItem> usedItems) {
        this.description = description;
        this.basePrice = basePrice;
        this.name = name;
        this.usedItems = usedItems;
    }

    @SuppressWarnings("unused")
    protected Contract() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double price) {
        this.basePrice = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UsedItem> getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(List<UsedItem> usedItems) {
        this.usedItems = usedItems;
    }
}
