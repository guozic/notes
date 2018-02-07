# Iterator

## 1.基本实现

- MyIterator2

  ```java
  package com.guozicheng;
  /**
   * 实现自己的迭代器
   * @author 张理
   */
  public interface MyIterator2 {
  		
  		void remove();//将游标指向下一个元素
  		boolean hasNext();  //判断是否有下个值
  		Object next();//获取当前游标指向的元素
  		
  }

  ```

  ​

- MyArrayList2

- - ​

  ```java
  package com.guozicheng;

  import java.util.ArrayList;
  import java.util.Iterator;
  import java.util.List;

  public class MyArrayList2  {
  	//在此简化list的实现
  	private List list = new ArrayList<>();
  	
  	public void addObject(Object obj) {
  		list.add(obj);
  	}
  	public void removeObject(Object obj) {
  		list.remove(obj);
  	}
  	public List<Object> getList() {
  		return list;
  	}
  	public void setList(List list) {
  		this.list = list;
  	}
  	
  	
  	//给集合添加一个方法   获取自己实现的迭代器
  	public MyIterator2 createMyIterator() {
  		return new implMyIterator2();
  	}
  	
  	//定义内部类，实现Iterator
  	private class implMyIterator2 implements MyIterator2 {

  		private int cursor;//游标当前位置
  		private int lastRet = -1;//当前游标前一个位置

  		@Override
  		public boolean hasNext() {
  			return cursor != list.size();
  		}

  		@Override
  		public Object next() {
  			Object object = list.get(cursor);
  			lastRet = cursor;
  			cursor++;
  			return object;
  		}

  		@Override
  		public void remove() {
  			list.remove(lastRet);
  		}
  	} 
  	
  	
  }
  ```

  ​

## 2.Iterator简介

### 2.1综述

- 是一种设计模式，迭代器模式，也叫游标模式。

- 是一个对象，它可以遍历并选择序列中的对象

- 轻量级”对象，因为创建它的代价小

- 每种集合具体的实现中都有实现Iterator接口的内部类，针对不同的集合，这个内部类里面提供了不同的访问这个集合内部元素的方式

  - ArrayList 

- 四人组给出的定义:提供一中方法，访问容器对象各个元素，而又不暴露该容器对象的内部细节

  - 定义中可见--->迭代器模式   因  容器而生。
  - foreach只能访问 有序集合中的元素，且暴露了内部细节

  ### 2.2各个具体的类与iterator的关系 

#### Iterator

- 接口，定义通用的迭代方式

```java
package java.util;
import java.util.function.Consumer;

public interface Iterator<E> { 
  
    boolean hasNext();
    E next();
  
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

#### Collection

- 接口，继承Iterator

```
public interface Collection<E> extends Iterable<E> {}
```



#### Iterable

- 接口，iterator为他的一个属性

```java

package java.lang;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;


public interface Iterable<T> {
   
    Iterator<T> iterator();

   
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

   
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}

```



#### List

- 接口，提供抽象方法获取Iterator

```
public interface List<E> extends Collection<E> {
    //属性
    Iterator<E> iterator();
}
```



#### ListIterator

- 作用与list 集合
- 继承Iterator,添加 了排序相关的方法

```java
package java.util;
public interface ListIterator<E> extends Iterator<E> { 
    boolean hasNext();
    E next();
    boolean hasPrevious();  
    E previous();
    int nextIndex();
    int previousIndex();
    void remove();
    void set(E e);
    void add(E e);
}
```



#### ArrayLIsti

- 两个内部类
  -  private class Itr implements Iterator<E> {}
  -  private class ListItr extends Itr implements ListIterator<E> {}


- 两个方法

  -  public ListIterator<E> listIterator()；
  -  public Iterator<E> iterator()；

  ​

```java
//方法一：获取listIterator
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

//方法二：获取Iterator   
    public Iterator<E> iterator() {
        return new Itr();
    }

//内部类一：Itr
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size) {
                return;
            }
            final Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

//内部类二： ListItr
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
```



#### linkdList

- 一个内部类
  -  private class DescendingIterator implements Iterator<E> {}
  -  实现Iterator
  -  底层维护的是ListItr
- 一个获取获取对应迭代器的方法
  - public Iterator<E> descendingIterator()；


- ```java
    
      public Iterator<E> descendingIterator() {
          return new DescendingIterator();
      }

      
      private class DescendingIterator implements Iterator<E> {
          private final ListItr itr = new ListItr(size());
          public boolean hasNext() {
              return itr.hasPrevious();
          }
          public E next() {
              return itr.previous();
          }
          public void remove() {
              itr.remove();
          }
      }
  ```



#### Set

- 接口，提供抽象方法获取Iterator

```
public interface Set<E> extends Collection<E> {
    //属性
    Iterator<E> iterator();
}
```

#### 



```

```













#### Map

- 遍历map的方式
  - 1.相加map对象转化为set对象--Set<Map.Entry<K, V>> entrySet();
    - 此时set集合中**每一个对象**就是map集合中的**每一对键值对**
  - 2.在通过set的迭代器进行迭代


- 一个方法
  -  Set<Map.Entry<K, V>> entrySet();

- 一个内部接口
  -  interface Entry<K,V> {}

  ```java
  package java.util;

  import java.util.function.BiConsumer;
  import java.util.function.BiFunction;
  import java.util.function.Function;
  import java.io.Serializable;


  public interface Map<K,V> {
      // Query Operations
      
      int size();
     
      boolean isEmpty();
      
      boolean containsKey(Object key);
     
      boolean containsValue(Object value);
      
      V get(Object key);

      // Modification Operations

      V put(K key, V value);
     
      V remove(Object key);

      void putAll(Map<? extends K, ? extends V> m);
     
      void clear();
      
      Set<K> keySet();
    
      Collection<V> values();
      
      Set<Map.Entry<K, V>> entrySet();
  //-------------接口
      interface Entry<K,V> {
        
          K getKey();

         
          V getValue();

         
          V setValue(V value);

         
          boolean equals(Object o);

          
          int hashCode();
       
          public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
              return (Comparator<Map.Entry<K, V>> & Serializable)
                  (c1, c2) -> c1.getKey().compareTo(c2.getKey());
          }
        
          public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K,V>> comparingByValue() {
              return (Comparator<Map.Entry<K, V>> & Serializable)
                  (c1, c2) -> c1.getValue().compareTo(c2.getValue());
          }
          
          public static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
              Objects.requireNonNull(cmp);
              return (Comparator<Map.Entry<K, V>> & Serializable)
                  (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
          }
        
          public static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
              Objects.requireNonNull(cmp);
              return (Comparator<Map.Entry<K, V>> & Serializable)
                  (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
          }
      }

      // Comparison and hashing

      boolean equals(Object o);
     
      int hashCode();
      
      default V getOrDefault(Object key, V defaultValue) {
          V v;
          return (((v = get(key)) != null) || containsKey(key))
              ? v
              : defaultValue;
      }

     
      default void forEach(BiConsumer<? super K, ? super V> action) {
          Objects.requireNonNull(action);
          for (Map.Entry<K, V> entry : entrySet()) {
              K k;
              V v;
              try {
                  k = entry.getKey();
                  v = entry.getValue();
              } catch(IllegalStateException ise) {
                  // this usually means the entry is no longer in the map.
                  throw new ConcurrentModificationException(ise);
              }
              action.accept(k, v);
          }
      }
     
      default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
          Objects.requireNonNull(function);
          for (Map.Entry<K, V> entry : entrySet()) {
              K k;
              V v;
              try {
                  k = entry.getKey();
                  v = entry.getValue();
              } catch(IllegalStateException ise) {
                  // this usually means the entry is no longer in the map.
                  throw new ConcurrentModificationException(ise);
              }

              // ise thrown from function is not a cme.
              v = function.apply(k, v);

              try {
                  entry.setValue(v);
              } catch(IllegalStateException ise) {
                  // this usually means the entry is no longer in the map.
                  throw new ConcurrentModificationException(ise);
              }
          }
      }
      
      default V putIfAbsent(K key, V value) {
          V v = get(key);
          if (v == null) {
              v = put(key, value);
          }

          return v;
      }
     
      default boolean remove(Object key, Object value) {
          Object curValue = get(key);
          if (!Objects.equals(curValue, value) ||
              (curValue == null && !containsKey(key))) {
              return false;
          }
          remove(key);
          return true;
      }
     
      default boolean replace(K key, V oldValue, V newValue) {
          Object curValue = get(key);
          if (!Objects.equals(curValue, oldValue) ||
              (curValue == null && !containsKey(key))) {
              return false;
          }
          put(key, newValue);
          return true;
      }
     
      default V replace(K key, V value) {
          V curValue;
          if (((curValue = get(key)) != null) || containsKey(key)) {
              curValue = put(key, value);
          }
          return curValue;
      }
     
      default V computeIfAbsent(K key,
              Function<? super K, ? extends V> mappingFunction) {
          Objects.requireNonNull(mappingFunction);
          V v;
          if ((v = get(key)) == null) {
              V newValue;
              if ((newValue = mappingFunction.apply(key)) != null) {
                  put(key, newValue);
                  return newValue;
              }
          }

          return v;
      }
    
      default V computeIfPresent(K key,
              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
          Objects.requireNonNull(remappingFunction);
          V oldValue;
          if ((oldValue = get(key)) != null) {
              V newValue = remappingFunction.apply(key, oldValue);
              if (newValue != null) {
                  put(key, newValue);
                  return newValue;
              } else {
                  remove(key);
                  return null;
              }
          } else {
              return null;
          }
      }
     
      default V compute(K key,
              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
          Objects.requireNonNull(remappingFunction);
          V oldValue = get(key);

          V newValue = remappingFunction.apply(key, oldValue);
          if (newValue == null) {
              // delete mapping
              if (oldValue != null || containsKey(key)) {
                  // something to remove
                  remove(key);
                  return null;
              } else {
                  // nothing to do. Leave things as they were.
                  return null;
              }
          } else {
              // add or replace old mapping
              put(key, newValue);
              return newValue;
          }
      }
      
      default V merge(K key, V value,
              BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
          Objects.requireNonNull(remappingFunction);
          Objects.requireNonNull(value);
          V oldValue = get(key);
          V newValue = (oldValue == null) ? value :
                     remappingFunction.apply(oldValue, value);
          if(newValue == null) {
              remove(key);
          } else {
              put(key, newValue);
          }
          return newValue;
      }
  }
  ```

  ​







