package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80));
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
//   Item("Conjured Mana Cake", 3, 6));

	@Test
	public void testUpdateEndOfDay_AgedBrie_Quality_10_11() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals(11, itemBrie.getQuality());
	}
	//sulfuras test
	@Test
	public void testUpdateEndOfDay_sulfuras() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80) );	
	
		store.updateEndOfDay();
		
		List<Item> items = store.getItems();
		Item itemSulfuras = items.get(0);
		assertEquals(80, itemSulfuras.getQuality());
		assertEquals(0, itemSulfuras.getSellIn());
		
	}
	//2 days pass and both values drop
	@Test
	public void testUpdateEndOfDay_values_go_down() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Elixir of the Mongoose", 5, 7) );
		
		store.updateEndOfDay();
		
		List<Item> items = store.getItems();
		Item item = items.get(0);
		assertEquals(4, item.getSellIn());
		assertEquals(6, item.getQuality());
		
		store.updateEndOfDay();
		
		assertEquals(3, item.getSellIn());
		assertEquals(5, item.getQuality());		
		
	}
	
	//after SellIn drops to 0 Quality drops twice as fast
	@Test
	public void testUpdateEndOfDay_Quality_drop_after_SellIn_0() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("+5 Dexterity Vest", 10, 20));
		
		store.updateEndOfDay();
		store.updateEndOfDay();		
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		
		List<Item> items = store.getItems();
		Item item = items.get(0);
		assertEquals(8, item.getQuality());	
		
		store.updateEndOfDay();
		
		assertEquals(6, item.getQuality());			
		
	}	

	//Quality is never negative
	@Test
	public void testUpdateEndOfDay_Quality_negative() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Conjured Mana Cake", 3, 6));
		
		store.updateEndOfDay();
		store.updateEndOfDay();		
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();
		store.updateEndOfDay();		
		store.updateEndOfDay();
		store.updateEndOfDay();
		
		List<Item> items = store.getItems();
		Item item = items.get(0);
		assertEquals("SellIn not 0 after 8 days",0, item.getSellIn());
		assertEquals("Quality not 0 after 8 days",0, item.getQuality());	
		
	}	
	
	//Quality does not exceed 50
	@Test
	public void testUpdateEndOfDay_Quality_does_not_exceed_50() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 0));
		
		for(int i=0;i<100;i++) {
		store.updateEndOfDay();	
		}
		
		List<Item> items = store.getItems();
		Item item = items.get(0);
		assertEquals("Quality is over 50",50, item.getQuality());	
	}		
	
	//Backstage pass works as described
	@Test
	public void testUpdateEndOfDay_backstage_pass() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
		
		store.updateEndOfDay();	
		List<Item> items = store.getItems();
		Item item = items.get(0);		
		
		assertEquals("SellIn not working as expected",14, item.getSellIn());
		assertEquals("Quality rised more than 1",21, item.getQuality());

		for(int i=0;i<4;i++) {
		store.updateEndOfDay();	
		}
		assertEquals("SellIn not working as expected",10, item.getSellIn());
		assertEquals("Quality rised more than 1",25, item.getQuality());
		
		store.updateEndOfDay();
		
		assertEquals("SellIn not working as expected",9, item.getSellIn());
		assertEquals("Quality rised more than 2",27, item.getQuality());
		
		for(int i=0;i<4;i++) {
		store.updateEndOfDay();	
		}
		
		assertEquals("SellIn not working as expected",5, item.getSellIn());
		assertEquals("Quality rised more than 2",35, item.getQuality());
		
		store.updateEndOfDay();
		
		assertEquals("SellIn not working as expected",4, item.getSellIn());
		assertEquals("Quality rised more than 3",38, item.getQuality());
		
		for(int i=0;i<4;i++) {
		store.updateEndOfDay();	
		}
		
		assertEquals("SellIn not working as expected",0, item.getSellIn());
		assertEquals("Quality rised more than 3",50, item.getQuality());
		
		store.updateEndOfDay();	
		
		assertEquals("Quality is not 90 after concert",0, item.getQuality());
		
	}	
	
}
