/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.batimen.web.app.utils;

import java.lang.reflect.Type;
import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for performing programmatic bean lookups.
 * 
 * @author Daniel Meyer, Cyril Casaucau
 */
public class ProgrammaticBeanLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammaticBeanLookup.class);

    private ProgrammaticBeanLookup() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(Class<T> clazz, BeanManager bm) {
        Iterator<Bean<?>> iter = bm.getBeans(clazz).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type "
                    + clazz.getName());
        }
        Bean<T> bean = (Bean<T>) iter.next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, ctx);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object lookup(String name, BeanManager bm) {
        Iterator<Bean<?>> iter = bm.getBeans(name).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type '" + name + "'");
        }
        Bean bean = iter.next();
        CreationalContext ctx = bm.createCreationalContext(bean);
        // select one beantype randomly. A bean has a non-empty set of
        // beantypes.
        Type type = (Type) bean.getTypes().iterator().next();
        return bm.getReference(bean, type, ctx);
    }

    public static <T> T lookup(Class<T> clazz) {
        BeanManager bm = null;
        try {
            bm = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Impossible de récuperer le context bean manager : java:comp/BeanManager ", e);
            }
        }
        if (bm != null) {
            return lookup(clazz, bm);
        } else {
            return null;
        }

    }

    public static Object lookup(String name) {
        BeanManager bm = null;
        try {
            bm = (BeanManager) new InitialContext().lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Impossible de récuperer le context bean manager : java:comp/BeanManager ", e);
            }
        }
        if (bm != null) {
            return lookup(name, bm);
        } else {
            return null;
        }
    }
}
