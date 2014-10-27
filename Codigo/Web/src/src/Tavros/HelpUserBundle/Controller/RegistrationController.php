<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of RegistrationController
 *
 * @author Ivan
 */

namespace Tavros\HelpUserBundle\Controller;

use Symfony\Component\HttpFoundation\RedirectResponse;
use FOS\UserBundle\Controller\RegistrationController as BaseController;
use Symfony\Component\HttpFoundation\Request;

class RegistrationController extends BaseController {

    public function registerAction() {
        $form = $this->container->get('fos_user.registration.form');
        $manipulator = $this->container->get('fos_user.util.user_manipulator');
        $formHandler = $this->container->get('fos_user.registration.form.handler');
        $confirmationEnabled = $this->container->getParameter('fos_user.registration.confirmation.enabled');

        $process = $formHandler->process($confirmationEnabled);
        if ($process) {
            $user = $form->getData();

            $this->container->get('logger')->info(
                    sprintf('New user registration: %s', $user)
            );

            if ($confirmationEnabled) {
                $this->container->get('session')->set('fos_user_send_confirmation_email/email', $user->getEmail());
                $route = 'fos_user_registration_check_email';
            } else {
                //KEEP SAME USER LOGGED IN
                //$this->authenticateUser($user);
                $route = 'fos_user_registration_confirmed';
            }

//            $this->get('session')->getFlashBag()->add('success', '¡Usuario creado con éxito!');

            $newRole = $this->container->get('request')->get('role');
            $manipulator->addRole($user->getUsername(), $newRole);

            $this->setFlash('success', 'registration.flash.user_created');

            //$url = $this->container->get('router')->generate($route);
            //CHANGE ROUTE AFTER SUCCESS REGISTRATION (GO HOME)
            $url = $this->container->get('router')->generate('tavros_administrator_home');

            return new RedirectResponse($url);
        }

        return $this->container->get('templating')->renderResponse('FOSUserBundle:Registration:register.html.twig', array(
                    'form' => $form->createView(),
        ));
    }

}

?>
