<?php

namespace Tavros\HelpUserBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{
    public function indexAction($name)
    {
        return $this->render('TavrosHelpUserBundle:Default:index.html.twig', array('name' => $name));
    }
}
