<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\AnonymousToken;
use \FOS\UserBundle\Entity\User as User;

class UserController extends Controller {

    //REGISTER A NEW USER//
    public function registerAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();

        if ($this->container->get('request')->getMethod() == 'POST') {
            $params = $_POST;
        } else {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
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
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        if ($userManager->findUserByEmail($email)) {
            $apiResponse->setCode(102);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $manipulator->create($username, $password, $email, !$inactive, $superadmin);
            $manipulator->addRole($username, $role);

            $apiResponse->setCode(200);
            $apiResponse->setPayload('');
            $response->setContent($serializer->serialize($apiResponse, 'json'));
        } catch (Exception $ex) {
            $apiResponse->setCode(103);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
        }

        return $response;
    }

    //RESET USER PASSWORD//
    public function resetAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $userManager = $this->container->get('fos_user.user_manager');
        $manipulator = $this->container->get('fos_user.util.user_manipulator');

        $request = $this->container->get('request');

        if ($request->getMethod() == 'POST') {
            $params = $_POST;
        } else {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $email = $params['email'];
        $user = $userManager->findUserByEmail($email);

        if (null === $user) {
            $apiResponse->setCode(107);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $newPassword = $this->randomPassword();

        try {
            $manipulator->changePassword($user->getUsername(), sha1($newPassword));
        } catch (Exception $ex) {
            $apiResponse->setCode(108);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
            return $response;
        }

        try {
            $message = \Swift_Message::newInstance()
                    ->setSubject('Nueva contraseña - [White Powder]')
                    ->setFrom('info@whitepowder.com')
                    ->setTo($email)
                    ->setBody(
                    $this->container->get('twig')->render(
                            'TavrosWebBundle:Emails:reset.txt.twig', array(
                        'name' => $user->getUsername(),
                        'password' => $newPassword)
            ));
            $this->container->get('mailer')->send($message);
        } catch (Exception $ex) {
            $apiResponse->setCode(109);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
            return $response;
        }

        $apiResponse->setCode(200);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //USER LOGIN//
    public function loginAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');

        $em = $this->container->get('Doctrine')->getManager();

        $request = $this->container->get('request');

        if (!$this->container->get('request')->getMethod() == 'POST') {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }else{
            $params = $_POST;
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

        $token = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByTokenUser($user);
        /* @var $token \Tavros\DomainBundle\Entity\Token */

        if (!$token) {
            $token = new \Tavros\DomainBundle\Entity\Token();
        }

        try {
            $token->setToken($this->loginUser($user));

            $apiResponse->setCode(200);
            $roles = $user->getRoles();

            foreach ($roles as $r) {

                if ($r === 'ROLE_SKIER') {
                    $role = 'ROLE_SKIER';
                    break;
                } elseif ($r === 'ROLE_RECON') {
                    $role = 'ROLE_RECON';
                    break;
                } else {
                    $role = 'ROLE_RESCU';
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

    //PASSWORD CHANGE//
    public function changePasswordAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $userManager = $this->container->get('fos_user.user_manager');
        $manipulator = $this->container->get('fos_user.util.user_manipulator');
        $em = $this->container->get('Doctrine')->getManager();

        $request = $this->container->get('request');

        if (!$request->getMethod() == 'POST') {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $content = json_decode($this->container->get('request')->getContent());

        $_token = $content->_token;
        $current_password = $content->current_password;
        $new_password = $content->new_password;

        $token = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByToken($_token);
        /*@var $token \Tavros\DomainBundle\Entity\Token */

        if (!$token) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }
        
        $user = $token->getTokenUser();
        
        if (!$this->checkUserPassword($user, $current_password)) {
            $apiResponse->setCode(113);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $manipulator->changePassword($user->getUsername(), $new_password);
//            $message = \Swift_Message::newInstance()
//                    ->setSubject('Cambio de contraseña - [White Powder]')
//                    ->setFrom('info@whitepowder.com')
//                    ->setTo($user->getEmail())
//                    ->setBody(
//                    $this->container->get('twig')->render(
//                            'TavrosWebBundle:Emails:change.txt.twig', array(
//                        'name' => $user->getUsername())
//            ));
//            $this->container->get('mailer')->send($message);
        } catch (Exception $ex) {
            $apiResponse->setCode(114);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            $logger->error('[TAVROS - ERROR]' . $ex);
            return $response;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload('');
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //USER LOGOUT//
    function logoutAction() {
        $this->logoutUser();
        return array('success' => true);
    }

    //////ADDITIONAL FUNCTIONS//////
    //GENERATES A RANDOM 6 LENGHT PASSWORD//
    protected function randomPassword() {
        $alphabet = "ABCDEFGHIJKLMNOPQRSTUWXYZ0123456789";
        $pass = array(); //remember to declare $pass as an array
        $alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
        for ($i = 0; $i < 6; $i++) {
            $n = rand(0, $alphaLength);
            $pass[] = $alphabet[$n];
        }
        return implode($pass); //turn the array into a string
    }

    //GET THE USER MANAGER//
    protected function getUserManager() {
        return $this->container->get('fos_user.user_manager');
    }

    //ACTUALLY AUTHENTICATE THE USER//
    protected function loginUser(User $user) {
        $security = $this->container->get('security.context');
        $providerKey = $this->container->getParameter('fos_user.firewall_name');
        $roles = $user->getRoles();
        $token = new UsernamePasswordToken($user, null, $providerKey, $roles);
        $security->setToken($token);

        $this->container->get('session')->set('_security_main', serialize($token));

        $csrf_token = $this->container->get('form.csrf_provider')->generateCsrfToken($user->getEmail() . date("D M d, Y G:i"));

        return $csrf_token;
    }

    //LOGOUT THE USER//
    protected function logoutUser() {
        $security = $this->container->get('security.context');
        $token = new AnonymousToken(null, new User());
        $security->setToken($token);
        $this->container->get('session')->invalidate();
    }

    //CHECK USER PASSWORD//
    protected function checkUserPassword(User $user, $password) {
        $factory = $this->container->get('security.encoder_factory');
        $encoder = $factory->getEncoder($user);
        if (!$encoder) {
            return false;
        }
        return $encoder->isPasswordValid($user->getPassword(), $password, $user->getSalt());
    }

}
