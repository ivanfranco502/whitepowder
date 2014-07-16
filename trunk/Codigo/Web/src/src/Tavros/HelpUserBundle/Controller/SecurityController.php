<?php

namespace Tavros\HelpUserBundle\Controller;

use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\AnonymousToken;
use FOS\UserBundle\Controller\SecurityController as BaseController;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use \FOS\UserBundle\Entity\User as User;

class SecurityController extends BaseController {

    protected function getUserManager() {
        return $this->container->get('fos_user.user_manager');
    }

    protected function loginUser(User $user) {
        $security = $this->container->get('security.context');
        $providerKey = $this->container->getParameter('fos_user.firewall_name');
        $roles = $user->getRoles();
        $token = new UsernamePasswordToken($user, null, $providerKey, $roles);
        $security->setToken($token);

        $this->container->get('session')->set('_security_main', serialize($token));

        $csrf_token = $this->container->get('form.csrf_provider')->generateCsrfToken('authenticate');

        return $csrf_token;
    }

    protected function logoutUser() {
        $security = $this->container->get('security.context');
        $token = new AnonymousToken(null, new User());
        $security->setToken($token);
        $this->container->get('session')->invalidate();
    }

    protected function checkUserPassword(User $user, $password) {
        $factory = $this->container->get('security.encoder_factory');
        $encoder = $factory->getEncoder($user);
        if (!$encoder) {
            return false;
        }
        return $encoder->isPasswordValid($user->getPassword(), $password, $user->getSalt());
    }

    public function loginAction() {

        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');

        $em = $this->container->get('Doctrine')->getManager();



        $request = $this->container->get('request');

        if ($request->getMethod() == 'POST') {
            $params = $_POST;
        } else {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $username = $params['username'];
        $password = $params['password'];

        $userManager = $this->getUserManager();
        $user = $userManager->findUserByUsername($username);

        if (!$user) {
            $user = $userManager->findUserByEmail($username);
        }

        if (!$user instanceof User) {
            $apiResponse->setCode(104);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        if (!$this->checkUserPassword($user, $password)) {
            $apiResponse->setCode(105);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $token = $em->getRepository('TavrosDomainBundle:Token')->findOneByTokenUser($user);
        /* @var $token \Tavros\DomainBundle\Entity\Token */

        if (!$token) {
            $token = new \Tavros\DomainBundle\Entity\Token();
        }

        try {
            $token->setToken($this->loginUser($user));

            $apiResponse->setCode(200);
            $roles = $user->getRoles();

            foreach ($roles as $r) {
                if ($r == 'ROLE_SKIER') {
                    $role = 'ROLE_SKIER';
                    break;
                }
            }

            $payload = array(
                "_token" => $token->getToken(),
                "role" => $role
            );
            $apiResponse->setPayload($payload);
            $token->setTokenUser($user);
            $em->persist($token);
            $em->flush();
            $response->setContent($serializer->serialize($apiResponse, 'json'));
        } catch (Exception $ex) {

            $apiResponse->setCode(106);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
        }

        return $response;
    }

    public function logoutAction() {
        $this->logoutUser();
        return array('success' => true);
    }

}
