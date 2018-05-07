package ru.siksmfp.spring.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.siksmfp.spring.security.entity.Role;
import ru.siksmfp.spring.security.entity.UserEntity;
import ru.siksmfp.spring.security.repository.impl.UserRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:app-context.xml"})
@WebAppConfiguration
public class SpringSecurityTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();

        UserEntity user1 = new UserEntity("email1", "name1", "secondName1", "1", Role.ADMIN);
        UserEntity user2 = new UserEntity("email2", "name2", "secondName2", "2", Role.USER);
        UserEntity user3 = new UserEntity("email3", "name3", "secondName3", "3", Role.USER);
        UserEntity user4 = new UserEntity("email4", "name4", "secondName4", "4", Role.ROOT);
        UserEntity user5 = new UserEntity("email5", "name5", "secondName5", "5", Role.USER);
        UserEntity user6 = new UserEntity("email6", "name6", "secondName6", "6", Role.USER);
        UserEntity user7 = new UserEntity("email7", "name7", "secondName7", "7", Role.USER);
        UserEntity user8 = new UserEntity("email8", "name8", "secondName8", "8", Role.ADMIN);

        List<UserEntity> userEntities = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8);
        userRepository.batchSave(userEntities);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void userExisting() {
        List<UserEntity> all = userRepository.getAll();

        Assert.assertEquals(8, all.size());
    }

    @Test
    public void findUserEntityByEmail() throws Exception {
        String email = "email1";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/find").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();
        UserEntity userFromController = (UserEntity) result.getModelAndView().getModel().get("result");
        UserEntity userFromRepository = userRepository.findUserByEmail(email);

        Assert.assertEquals(userFromController, userFromRepository);
    }

    @Test
    public void notFindUserEntityByEmail() throws Exception {
        String email = "email100";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/find").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();
        UserEntity userFromController = (UserEntity) result.getModelAndView().getModel().get("result");
        UserEntity userFromRepository = userRepository.findUserByEmail(email);

        Assert.assertNull(userFromController);
        Assert.assertNull(userFromRepository);
    }

    @Test
    public void deleteUserEntityByEmail() throws Exception {
        String email = "email1";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/delete").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/find").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userFromController = (UserEntity) result.getModelAndView().getModel().get("result");
        UserEntity userFromRepository = userRepository.findUserByEmail(email);

        Assert.assertNull(userFromController);
        Assert.assertNull(userFromRepository);
    }

    @Test
    public void notDeleteUserEntityByEmail() throws Exception {
        String email = "email100";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/delete").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/find").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userFromController = (UserEntity) result.getModelAndView().getModel().get("result");
        UserEntity userFromRepository = userRepository.findUserByEmail(email);

        Assert.assertNull(userFromController);
        Assert.assertNull(userFromRepository);
    }

    @Test
    public void createUserEntityByEmail() throws Exception {
        String email = "email10";
        String firstName = "firstName";
        String secondName = "secondName";
        String password = "password";

        mockMvc.perform(MockMvcRequestBuilders
                .get("/save")
                .param("email", email)
                .param("firstName", firstName)
                .param("secondName", secondName)
                .param("password", password)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/find").param("email", email)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();

        UserEntity userFromController = (UserEntity) result.getModelAndView().getModel().get("result");
        UserEntity userFromRepository = userRepository.findUserByEmail(email);

        Assert.assertEquals(userFromController, userFromRepository);
    }
}
