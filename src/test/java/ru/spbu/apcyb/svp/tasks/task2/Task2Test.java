package ru.spbu.apcyb.svp.tasks.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.spbu.apcyb.svp.tasks.task2.DoublyLinkedList;

/**
 * Тесты для задания 2.
 */
public class Task2Test {

  @Test
  void addObjectTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    assertTrue(list.add(12));
    assertEquals(1, list.size());
  }

  @Test
  void addSeveralObjectsTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    list.add(1);
    list.add(2);
    list.add("123");
    assertEquals(3, list.size());
  }

  @Test
  void getTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    list.add(1);
    list.add(2);
    Object result = list.get(1);
    assertEquals(2, result);
  }

  @Test
  void getFromEmptyListTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    Object element = list.get(0);
    assertNull(element);
  }

  @Test
  void addAllTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.add(1);
    list1.add(2);
    list1.add(3);
    DoublyLinkedList list2 = new DoublyLinkedList();
    list2.addAll(List.of(1, 2, 3));
    for (int i = 0; i < list1.size(); i++) {
      assertEquals(list1.get(i), list2.get(i));
    }
  }

  @Test
  void isListEmptyTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.add("123");
    DoublyLinkedList list2 = new DoublyLinkedList();
    assertFalse(list1.isEmpty());
    assertTrue(list2.isEmpty());
  }

  @Test
  void searchTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    list.addAll(new ArrayList<>(List.of(1, 5, 8, 4, 3)));
    int index = list.indexOf(5);
    assertEquals(1, index);
  }

  @Test
  void elementNotFoundTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    list.addAll(new ArrayList<>(List.of(1, 5, 8, 4, 3)));
    int index = list.indexOf(-15);
    assertEquals(-1, index);
  }

  @Test
  void removeElementTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.addAll(new ArrayList<>(List.of(1, 5, 8, 4, 3)));
    Object result = list1.remove(2);

    DoublyLinkedList list2 = new DoublyLinkedList();
    list2.addAll(new ArrayList<>(List.of(1, 5, 4, 3)));
    for (int i = 0; i < list1.size(); i++) {
      assertEquals(list1.get(i), list2.get(i));
    }
    assertEquals(8, result);
  }

  @Test
  void removeElementOnTailTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.addAll(new ArrayList<>(List.of(1, 5, 8, 4, 3)));
    Object result = list1.remove(4);

    DoublyLinkedList list2 = new DoublyLinkedList();
    list2.addAll(new ArrayList<>(List.of(1, 5, 8, 4)));
    for (int i = 0; i < list1.size(); i++) {
      assertEquals(list1.get(i), list2.get(i));
    }
    assertEquals(3, result);
  }

  @Test
  void removeHeadTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.addAll(new ArrayList<>(List.of(1, 5, 8, 4, 3)));
    Object result = list1.remove(0);

    DoublyLinkedList list2 = new DoublyLinkedList();
    list2.addAll(new ArrayList<>(List.of(5, 8, 4, 3)));
    for (int i = 0; i < list1.size(); i++) {
      assertEquals(list1.get(i), list2.get(i));
    }
    assertEquals(1, result);
  }

  @Test
  void removeLastElementTest() {
    DoublyLinkedList list1 = new DoublyLinkedList();
    list1.addAll(new ArrayList<>(List.of(1)));
    Object result = list1.remove(0);
    assertEquals(1, result);
    assertEquals(0, list1.size());
  }

  @Test
  void removeFromEmptyListTest() {
    DoublyLinkedList list = new DoublyLinkedList();
    assertThrows(IndexOutOfBoundsException.class, () -> {
      list.remove(0);
    });
  }

  @Test
  void containsElementTest() {
    DoublyLinkedList list = new DoublyLinkedList(List.of(1, 5, 8, 4, 3));
    assertTrue(list.contains(5));
    assertFalse(list.contains(-15));
  }

  @Test
  void containsAllTest() {
    DoublyLinkedList list = new DoublyLinkedList(List.of(1, 5, 8, 4, 3));
    assertTrue(list.containsAll(List.of(5,3)));
    assertFalse(list.containsAll(List.of(0, -15, 9)));
  }

}
