<?php

namespace Tavros\HelpUserBundle\Controller;

use FOS\UserBundle\Controller\RegistrationController as BaseController;
use Symfony\Component\HttpFoundation\JsonResponse;

class RegistrationController extends BaseController {

    public function registerAction() {
//        DEBEN LLEGAR LOS SIGUIENTES CAMPOS:
//        fos_user_registration_form_username
//        fos_user_registration_form_email
//        fos_user_registration_form_plainPassword_first
//        fos_user_registration_form_plainPassword_second
//        fos_user_registration_form__token
        $form = $this->container->get('fos_user.registration.form');
        $form['username'] = $_POST('fos_user_registration_form_username');
        $form['email'] = $_POST['fos_user_registration_form_email'];
        $form['plainPassword']['first'] = $request->get('fos_user_registration_form_plainPassword_first');
        $form['plainPassword']['second'] = $request->get('fos_user_registration_form_plainPassword_second');
        $form['_token'] = $request->get('fos_user_registration_form__token');

        $formHandler = $this->container->get('fos_user.registration.form.handler');
        $confirmationEnabled = $this->container->getParameter('fos_user.registration.confirmation.enabled');

        $process = $formHandler->process($confirmationEnabled);
        if ($process) {
            $user = $form->getData();

            $authUser = false;
            if ($confirmationEnabled) {
                $this->container->get('session')->set('fos_user_send_confirmation_email/email', $user->getEmail());
                $route = 'fos_user_registration_check_email';
            } else {
                $authUser = true;
                $route = 'fos_user_registration_confirmed';
            }

            $this->setFlash('fos_user_success', 'registration.flash.user_created');
            $url = $this->container->get('router')->generate($route);
            $response = new RedirectResponse($url);

            if ($authUser) {
                $this->authenticateUser($user, $response);
            }

            return $response;
        }

        $response = new JsonResponse(array(
            'status' => "ERROR",
            'code' => 1,
            'message' => "All the information should be provided."));

        return $response;
//        return $this->container->get('templating')->renderResponse('FOSUserBundle:Registration:register.html.' . $this->getEngine(), array(
//                    'form' => $form->createView(),
//        ));
    }

}
