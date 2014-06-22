<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{
    public function indexAction($name)
    {
        return $this->render('TavrosInternalApiBundle:Default:index.html.twig', array('name' => $name));
    }
    
}
