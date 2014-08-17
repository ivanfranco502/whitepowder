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

    public function centerAction() {
        $em = $this->container->get('Doctrine')->getManager();
        $generalInformationDTO = $em->getRepository('TavrosDomainBundle:GeneralInformationDTO')->findAll();
        if(!$generalInformationDTO){
            return $this->render('TavrosWebBundle:Administrator:center.html.twig', 
                                array('coordinate_x' => 10,
                                        'coordinate_y' => 50));
        }else{
        return $this->render('TavrosWebBundle:Administrator:center.html.twig', 
                                array('coordinate_x' => $generalInformationDTO[0]->getGeinX(),
                                        'coordinate_y' => $generalInformationDTO[0]->getGeinY()));
        }
    }

}
