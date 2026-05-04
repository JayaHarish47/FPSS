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

package de.tud.st.springpetclinic.system.storage;

import de.tud.st.springpetclinic.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Karl Kegel
 */
@Entity
@Table(name = "items")
public class Item extends BaseEntity {

	private String description;

	private int inStock;

	private double price;

	public Item(String description, int inStock, double price) {
		this.description = description;
		this.inStock = inStock;
		this.price = price;
	}

	@SuppressWarnings("unused")
	protected Item() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
