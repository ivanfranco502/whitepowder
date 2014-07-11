<?php

namespace Tavros\HelpUserBundle\Controller;

use FOS\UserBundle\Controller\RegistrationController as BaseController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Tavros\InternalApiBundle\Entity\ApiResponse;

class RegistrationController extends BaseController {

    public function registerAction() {

        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new JsonResponse();

        if ($this->container->get('request')->getMethod() == 'POST') {
            $params = $_POST;
        } else {
            $apiResponse->setCode(404);
            $response->setData($serializer->serialize($apiResponse, 'json'));
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
            $apiResponse->setCode(101);
            $response->setData($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        if ($userManager->findUserByEmail($email)) {
            $apiResponse->setCode(102);
            $response->setData($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $manipulator->create($username, $password, $email, !$inactive, $superadmin);
            $manipulator->addRole($username, $role);

            $apiResponse->setCode(200);
            $apiResponse->setPayload('');
            $response->setData($serializer->serialize($apiResponse, 'json'));
        } catch (Exception $ex) {
            $apiResponse->setCode(103);
            $response->setData($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
        }

        return $response;
    }

}
