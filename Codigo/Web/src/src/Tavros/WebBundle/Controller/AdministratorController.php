<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class AdministratorController extends Controller {

    public function indexAction() {
        
        return $this->render('TavrosWebBundle:Administrator:index.html.twig');
    }

}
