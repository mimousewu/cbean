package org.exolab.castor.mapping.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;

public final class SeqMapCollectionHandlers {
	public static CollectionHandlers.Info[] getCollectionHandlersInfo()
    {
        return _colHandlers;
    }


    /**
     * List of all the default collection handlers.
     */
    private static CollectionHandlers.Info[] _colHandlers = new CollectionHandlers.Info[] {
        new CollectionHandlers.Info( "arraylist", ArrayList.class, false, new CollectionHandler() {
            @SuppressWarnings("unchecked")
			public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new ArrayList();
                    ( (Collection) collection ).add( object );
                    return collection;
                } else {
                    ( (Collection) collection ).add( object );
                    return null;
                }
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Collection) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Collection) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Collection) collection ).clear();
                return null;
            }
            public String toString() {
                return "ArrayList";
            }
        } ),
        // For Collection/ArrayList (1.2)
        new CollectionHandlers.Info( "collection", Collection.class, false, new CollectionHandler() {
            @SuppressWarnings("unchecked")
			public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new ArrayList();
                    ( (Collection) collection ).add( object );
                    return collection;
                } else {
                    ( (Collection) collection ).add( object );
                    return null;
                }
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Collection) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Collection) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Collection) collection ).clear();
                return null;
            }
            public String toString() {
                return "Collection";
            }
        } ),
        // For Set/HashSet (1.2)
        new CollectionHandlers.Info( "set", Set.class, false, new CollectionHandler() {
            @SuppressWarnings("unchecked")
			public Object add( Object collection, Object object ) {
                if ( collection == null ) {
                    collection = new HashSet();
                    ( (Set) collection ).add( object );
                    return collection;
                } else {
                    //if ( ! ( (Set) collection ).contains( object ) )
                    ( (Set) collection ).add( object );
                    return null;
                }
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Set) collection ).iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Set) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Set) collection ).clear();
                return null;
            }
            public String toString() {
                return "Set";
            }
        } ),
        // For Map/HashMap (1.2)
        new CollectionHandlers.Info( "map", Map.class, false, new CollectionHandler() {
            @SuppressWarnings("unchecked")
			public Object add( Object collection, Object object ) {
                
                Object key = object;
                Object value = object;
                
                if (object instanceof MapItem) {
                    MapItem item = (MapItem)object;
                    key = item.getKey();
                    value = item.getValue();
                    if (value == null) {
                        value = object;
                    }
                    if (key == null) {
                        key = value;
                    }
                }
                
                if ( collection == null ) {
                    collection = new LinkedHashMap();
                    ( (Map) collection ).put( key, value );
                    return collection;
                } else {
                    ( (Map) collection ).put( key, value );
                    return null;
                }
            }
            public Enumeration elements( Object collection ) {
                if ( collection == null )
                    return new CollectionHandlers.EmptyEnumerator();
                return new IteratorEnumerator( ( (Map) collection ).values().iterator() );
            }
            public int size( Object collection )
            {
                if ( collection == null )
                    return 0;
                return ( (Map) collection ).size();
            }
            public Object clear( Object collection ) {
                if ( collection != null )
                    ( (Map) collection ).clear();
                return null;
            }
            public String toString() {
                return "Map";
            }
        } )
    };


    /**
     * Enumerator for an iterator.
     */
    static final class IteratorEnumerator
        implements Enumeration
    {

        private final Iterator _iterator;

        IteratorEnumerator( Iterator iterator )
        {
            _iterator = iterator;
        }

        public boolean hasMoreElements()
        {
            return _iterator.hasNext();
        }

        public Object nextElement()
        {
            return _iterator.next();
        }

    }
}
