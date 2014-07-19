<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller {

    public function indexAction() {
        return $this->render('TavrosWebBundle:Web:index.html.twig');
    }

}
