package org.geoserver.rest.wrapper;

import freemarker.template.Template;
import org.geoserver.rest.converters.FreemarkerHTMLMessageConverter;
import org.geoserver.rest.converters.XStreamCatalogListConverter;

import java.util.Collection;

/**
 * A wrapper for all Collection type responses using the {@link XStreamCatalogListConverter}
 * (XML and JSON output).
 * Also supports Collection type responses using the {@link FreemarkerHTMLMessageConverter},
 * but is not required for such responses.
 *
 * In the previous rest API this wasn't needed because in each individual rest request the Collections were aliased to
 */
public class RestListWrapper<T> extends RestWrapperAdapter<T> {


    public RestListWrapper(Collection<T> collection, Class<T> clazz, Template template) {
        super(collection, clazz, template);
    }

    /**
     * Alias for {@link #getObject()}
     */
    public Collection<T> getCollection() {
        return (Collection<T>) getObject();
    }
}
