package com.laketravels.ch11.service.endpoint;

import com.laketravels.ch11.service.util.ESUtil;
import com.laketravels.ch08.model.data.Address;
import com.laketravels.ch08.model.data.Contact;
import com.laketravels.ch08.model.data.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
@EnableAutoConfiguration
@Api(value = "customer", description = "This is the customer resource that provides"
        + " customer information ")
@Path("customer")
public class CustomerEndpoint {

    private ObjectMapper MAPPER  = new ObjectMapper();

    public CustomerEndpoint() {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Autowired
    private ESUtil esUtil;


    private Logger logger = LoggerFactory.getLogger(CustomerEndpoint.class);


    @Path("{customerId}")
    @GET
    @ApiOperation(value = "profile", consumes = MediaType.TEXT_PLAIN, produces = MediaType.APPLICATION_JSON,
            response = Customer.class,
            notes = "This endpoint is used to get customer profile")
    public Customer getCustomer(@PathParam("customerId") String customerId) throws IOException, ParseException {
        return getCustomerProfile(customerId);
    }

    @Path("{customerId}/address")
    @GET
    @ApiOperation(value = "address", consumes = MediaType.TEXT_PLAIN, produces = MediaType.APPLICATION_JSON,
            response = Address.class,
            notes = "This endpoint is used to get customer address")
    public Address getAddress(@ApiParam(
            name = "customerId",
            required = true,
            value = "Contains customer Id"
    ) @PathParam("customerId") String customerId) throws IOException {
        return getCustomerAddress(customerId);
    }

    @Path("{customerId}/contact")
    @GET
    @ApiModelProperty(required = true, dataType = "org.joda.time.LocalDate")
    @ApiOperation(value = "contact", consumes = MediaType.TEXT_PLAIN, produces = MediaType.APPLICATION_JSON,
            response = Contact.class,
            notes = "This endpoint is used to get customer contact")
    public Contact getContact(@ApiParam(
            name = "customerId",
            required = true,
            value = "Contains customer Id"
    ) @PathParam("customerId") String customerId) throws IOException {
        return getCustomerContact(customerId);
    }

    private Customer getCustomerProfile(String customerId) throws IOException {
        SearchRequestBuilder reqBuilder = esUtil.getConnection().prepareSearch();
        reqBuilder.setIndices("customer").setTypes("customer");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("id", customerId);
        reqBuilder.setQuery(termQueryBuilder);
        Customer customer = MAPPER.readValue(reqBuilder.get().getHits().getAt(0).getSourceAsString(),
                Customer.class);
        customer.setAddress(getCustomerAddress(customerId));
        customer.setContact(getCustomerContact(customerId));
        System.out.println(MAPPER.writeValueAsString(customer));
        return customer;
    }

    private Address getCustomerAddress(String customerId) throws IOException {
        SearchRequestBuilder reqBuilder = esUtil.getConnection().prepareSearch();
        reqBuilder.setIndices("address").setTypes("address");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("id", customerId);
        reqBuilder.setQuery(termQueryBuilder);
        Address address = MAPPER.readValue(reqBuilder.get().getHits().getAt(0).getSourceAsString(),
                Address.class);
        return address;
    }

    private Contact getCustomerContact(String customerId) throws IOException {
        SearchRequestBuilder reqBuilder = esUtil.getConnection().prepareSearch();
        reqBuilder.setIndices("contacts").setTypes("contact");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("id", customerId);
        reqBuilder.setQuery(termQueryBuilder);
        Contact contact = MAPPER.readValue(reqBuilder.get().getHits().getAt(0).getSourceAsString(),
                Contact.class);
        return contact;
    }
}