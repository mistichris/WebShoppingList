package controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.ListItem;

/**
 * @author Misti Christianson - mchristianson CIS175 - Spring 2024 Feb 1, 2024
 */
public class ListItemHelper {
	static EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("WebShoppingList");

	//add an item to the database/table
	public void insertItem(ListItem li) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		em.persist(li);
		em.getTransaction().commit();
		em.close();
	}

	//show all items in the database/table
	@SuppressWarnings("unchecked")
	public List<ListItem> showAllItems(){
		EntityManager em = emfactory.createEntityManager();
		List<ListItem> allItems = em.createQuery("SELECT i FROM ListItem i").getResultList();
		return allItems;
		}

	//delete an item from the database
	public void deleteItem(ListItem toDelete) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery(
				"Select li from ListItem li where li.store = :selectedStore and li.item = :selectedItem",
				ListItem.class);

		// Substitute parameter with actual data from the toDelete item
		typedQuery.setParameter("selectedStore", toDelete.getStore());
		typedQuery.setParameter("selectedItem", toDelete.getItem());

		// we only want on result
		typedQuery.setMaxResults(1);

		// get the result and save it into a new list item
		ListItem result = typedQuery.getSingleResult();

		// remove it
		em.remove(result);
		em.getTransaction().commit();
		em.close();
	}
	
		//update item in database script
	public void updateItem(ListItem toEdit) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		em.merge(toEdit);
		em.getTransaction().commit();
		em.close();
		}
	
		//search for item by store script
	public List<ListItem> searchForItemByStore(String storeName) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery("select li from ListItem li where li.store = :selectedStore", ListItem.class);
		typedQuery.setParameter("selectedStore", storeName);
		List<ListItem> foundItems = typedQuery.getResultList();
		em.close();
		return foundItems;
		}
	
		//search for item by name script
	public List<ListItem> searchForItemByItem(String itemName) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		TypedQuery<ListItem> typedQuery = em.createQuery("select li from ListItem li where li.item = :selectedItem", ListItem.class);
		typedQuery.setParameter("selectedItem", itemName);
		List<ListItem> foundItems = typedQuery.getResultList();
		em.close();
		return foundItems;
		}
	
	//Search for Item by ID script
	public ListItem searchForItemById(int idToEdit) {
		EntityManager em = emfactory.createEntityManager();
		em.getTransaction().begin();
		ListItem found = em.find(ListItem.class, idToEdit);
		em.close();
		return found;
		}

	
	//closing Entity Manager Factory
	public void cleanUp(){
		emfactory.close();
		}
}
