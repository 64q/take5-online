package net.take5.backend.controller;

import java.util.ArrayList;
import java.util.List;

import net.take5.commons.pojo.output.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class DefaultController
{
    @RequestMapping(value = "list/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> listUsersAction()
    {
        List<User> usersList = new ArrayList<User>();

        User user1 = new User();

        user1.setUsername("test");
        user1.setLostGames(10L);
        user1.setWonGames(10L);

        usersList.add(user1);

        return usersList;
    }
}
