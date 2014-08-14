<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class AdministratorController extends Controller {

    public function indexAction() {

        return $this->render('TavrosWebBundle:Administrator:index.html.twig', Array('home_active' => 'active'));
    }

    public function aboutAction() {

        return $this->render('TavrosWebBundle:Administrator:about.html.twig', Array('about_active' => 'active'));
    }

}
