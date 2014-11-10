package net.take5.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Contrôleur principal du frontend
 * 
 * @author Quentin
 */
@Controller
@RequestMapping("/")
public class DefaultController
{
    /**
     * Méthode appelée par défaut
     * 
     * @return la vue default.jsp
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView defaultAction()
    {
        return new ModelAndView("default");
    }
}
