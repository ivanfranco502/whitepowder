<?php

namespace Tavros\HelpUserBundle\Controller;

use FOS\UserBundle\Controller\RegistrationController as BaseController;
use Symfony\Component\HttpFoundation\JsonResponse;

class RegistrationController extends BaseController {

    public function registerAction() {
        
        $logger = $this->container->get('logger');
        
        if ($this->container->get('request')->getMethod() == 'POST') {
            $params = $_POST;
        } else {
            $response = new JsonResponse(array(
                'status' => "ERROR",
                'code' => 100,
                'message' => "Método no permitido."));
            return $response;
        }
        
        $username = $params['username'];
        $email = $params['email'];
        $password = $params['password'];
        $inactive = $params['inactive'];
        $superadmin = $params['superadmin'];
        $role = $params['role'];

        $manipulator = $this->container->get('fos_user.util.user_manipulator');
        $userManager = $this->container->get('fos_user.user_manager');

        if ($userManager->findUserByUsername($username)) {
            $response = new JsonResponse(array(
                'status' => "ERROR",
                'code' => 101,
                'message' => "El nombre de usuario ya se encuentra registrado."));
            return $response;
        }

        if ($userManager->findUserByEmail($email)) {
            $response = new JsonResponse(array(
                'status' => "ERROR",
                'code' => 102,
                'message' => "El email ya se encuentra registrado."));
            return $response;
        }

        try {
            $manipulator->create($username, $password, $email, !$inactive, $superadmin);
            $manipulator->addRole($username, $role);
        } catch (Exception $ex) {
            $response = new JsonResponse(array(
                'status' => "ERROR",
                'code' => 103,
                'message' => "No se pudo crear el usuario, vuelva a intentarlo."));
            $logger->error('[TAVROS - ERROR]' . $ex);
            return $response;
        }

        $response = new JsonResponse(array(
            'status' => "SUCCESS",
            'code' => 001,
            'message' => "El usuario fue creado con éxito."));

        return $response;
    }

}
