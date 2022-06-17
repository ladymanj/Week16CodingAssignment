/**
 * 
 */
package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

/**
 * @author Jordan
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8")) 
class CreateOrderTest {
  
  @LocalServerPort
  private int serverPort;
  
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testCreateOrderReturnsSuccess201() {
    // Given: an order as JSON
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
    
    // When: the order is sent
    ResponseEntity<Order> response = restTemplate.exchange(uri, HttpMethod.POST, bodyEntity, Order.class);
    
    // Then: a 201 status is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // And: the returned order is correct
    assertThat(response.getBody()).isNotNull();

    Order order = response.getBody();
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("MAYNARD_TORBJORG");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.COMPASS);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("Trailhawk");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_HELLAYELLA");
    assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_HYBRID");
    assertThat(order.getTire().getTireId()).isEqualTo("275_BFGOODRICH");
    assertThat(order.getOptions()).hasSize(6);
  }
  
  protected String createOrderBody() {
    // @formatter:off
    return "{\n"
        + "  \"customer\":\"MAYNARD_TORBJORG\",\n"
        + "  \"model\":\"COMPASS\",\n"
        + "  \"trim\":\"Trailhawk\",\n"
        + "  \"doors\":4,\n"
        + "  \"color\":\"EXT_HELLAYELLA\",\n"
        + "  \"engine\":\"2_0_HYBRID\",\n"
        + "  \"tire\":\"275_BFGOODRICH\",\n"
        + "  \"options\":[\n"
        + "    \"DOOR_QUAD_4\",\n"
        + "    \"EXT_AEV_LIFT\",\n"
        + "    \"EXT_WARN_WINCH\",\n"
        + "    \"EXT_WARN_BUMPER_FRONT\",\n"
        + "    \"EXT_WARN_BUMPER_REAR\",\n"
        + "    \"EXT_ARB_COMPRESSOR\"\n"
        + "  ]\n"
        + "}";
    // @formatter:on
  }

}
