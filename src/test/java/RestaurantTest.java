import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RestaurantTest {

   Restaurant restaurant;
    @BeforeEach
    public void setUp() {
        RestaurantService service = new RestaurantService();
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = service.addRestaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);

    }
//Refactor the code
    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
      //  LocalTime openingTime = LocalTime.parse("10:30:00");
      //  LocalTime closingTime = LocalTime.parse("22:00:00");
       // restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        Restaurant spyRestarunts=Mockito.spy(restaurant);
        LocalTime time =LocalTime.parse("11:30:00");
        Mockito.when(spyRestarunts.getCurrentTime()).thenReturn(time);
        assertTrue(spyRestarunts.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){

        Restaurant spyRestarunts=Mockito.spy(restaurant);
        LocalTime time =LocalTime.parse("23:30:00");
        Mockito.when(spyRestarunts.getCurrentTime()).thenReturn(time);
        assertFalse(spyRestarunts.isRestaurantOpen());
        Mockito.verify(spyRestarunts).getCurrentTime();

    }

    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Test
    public void get_total_order_should_be_equal_to_addition_of_price_from_ordered_menu(){
        List<String> item_name=new ArrayList<>();
        item_name.add("Sweet corn soup");
        item_name.add("Vegetable lasagne");
        int price=restaurant.getTotalOrderAmount(item_name);
        List<Item >menu =  restaurant.getMenu();
        assertEquals(388,price);
    }
    @Test
    public void total_order_value_should_be_zero_when_cart_is_empty(){
        List<String> selectedItems = new ArrayList<String>();
        assertEquals(0,restaurant.getTotalOrderAmount(selectedItems));
    }

}