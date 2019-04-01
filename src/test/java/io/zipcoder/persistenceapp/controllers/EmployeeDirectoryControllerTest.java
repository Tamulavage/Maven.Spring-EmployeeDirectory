package io.zipcoder.persistenceapp.controllers;

import io.zipcoder.persistenceapp.entities.Employee;
import io.zipcoder.persistenceapp.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EmployeeDirectoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Before
    public void initTest() {

    }

    @Test
    public void testShowEmployee() throws Exception {
        Integer givenId = 1;
        BDDMockito
                .given(employeeRepository.findOne(givenId))
                .willReturn(new Employee(givenId, "David", "T", "Associate", null, null, null, 2, 2));

        String expectedContent = "{\"id\":1," +
                "\"fname\":\"David\"," +
                "\"lname\":\"T\"," +
                "\"title\":\"Associate\"," +
                "\"phoneNumber\":null," +
                "\"email\":null," +
                "\"hireDate\":null," +
                "\"managerId\":2," +
                "\"departmentNumber\":2}";
        this.mvc.perform(MockMvcRequestBuilders
                .get("/Employee/" + givenId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee(1, "David", "T", "Associate", "1", "2", "3", 2, 2);
        BDDMockito
                .given(employeeRepository.save(employee))
                .willReturn(employee);


        String expectedContent = "{\"id\":1," +
                "\"fname\":\"David\"," +
                "\"lname\":\"T\"," +
                "\"title\":\"Associate\"," +
                "\"phoneNumber\":\"1\"," +
                "\"email\":\"2\"," +
                "\"hireDate\":\"3\"," +
                "\"managerId\":2," +
                "\"departmentNumber\":2}";
        this.mvc.perform(MockMvcRequestBuilders
                        .post("/Employee/")
                        .content(expectedContent)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(expectedContent))
                // use below if want to compare line by line
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                //.andExpect(jsonPath("$.title").value("Associate"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testUpdateEmployee() throws Exception
    {
        Employee employee = new Employee(10, "David", "T", "Associate", "1", "2", "3", 2, 2);
        BDDMockito
                .given(employeeRepository.findOne(10))
                .willReturn(employee);

        String expectedContent = "{\"id\":10," +
                "\"fname\":\"David\"," +
                "\"lname\":\"T\"," +
                "\"title\":\"Manager\"," +
                "\"phoneNumber\":\"1\"," +
                "\"email\":\"2\"," +
                "\"hireDate\":\"3\"," +
                "\"managerId\":2," +
                "\"departmentNumber\":2}";
        this.mvc.perform(MockMvcRequestBuilders
                .put("/Employee/")
                .content(expectedContent)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
           //     .andExpect(MockMvcResultMatchers.content().string(expectedContent))
                ;

    }


}