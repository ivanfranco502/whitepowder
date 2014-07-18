<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class AdministratorController extends Controller {

    public function indexAction($name = "joaquin") {
        
        return $this->render('TavrosWebBundle:Default:index.html.twig', array('name' => $name));
    }

}
