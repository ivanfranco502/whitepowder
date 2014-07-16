<?php

namespace Tavros\HelpUserBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\DependencyInjection\ContainerAware;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Security\Core\Exception\AccountStatusException;
use FOS\UserBundle\Model\UserInterface;
use FOS\UserBundle\Controller\ResettingController as BaseController;
use Tavros\InternalApiBundle\Entity\ApiResponse;

class ResettingController extends BaseController {

    /**
     * Reset user password
     */
    public function resetAction($token = '') {
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
                            'TavrosHelpUserBundle:Emails:reset.txt.twig', array(
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

}
