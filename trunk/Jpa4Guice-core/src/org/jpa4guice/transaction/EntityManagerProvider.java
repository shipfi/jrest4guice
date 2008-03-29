/**
 * Copyright (C) 2008 Wideplay Interactive.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jpa4guice.transaction;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;


class EntityManagerProvider implements Provider<EntityManager> {
    private final EntityManagerFactoryHolder holder;

    @Inject
    public EntityManagerProvider(EntityManagerFactoryHolder holder) {
        this.holder = holder;
    }

    public EntityManager get() {
        return holder.getEntityManager();
    }
}
