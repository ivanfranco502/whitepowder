<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use Tavros\DomainBundle\Entity\Coordinate as Coordinate;
use Tavros\DomainBundle\Entity\UserCoordinate as UserCoordinate;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\AnonymousToken;
use \FOS\UserBundle\Entity\User as User;

class SkierController extends Controller {

    //SET CURRENT POSITION
    public function setPositionAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $em = $this->container->get('Doctrine')->getManager();

        if (!$this->container->get('request')->getMethod() == 'POST') {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $content = json_decode($this->container->get('request')->getContent());

        /* @var $token \Tavros\DomainBundle\Entity\Token */

        $token = $em->getRepository('TavrosDomainBundle:Token')->findOneByToken($content->_token);

        if (!$token) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $coord_xy = $content->coordinate;
            $coord = new Coordinate();
            $userCoord = new UserCoordinate();

            $coord->setCoorCreatedDate(new \DateTime(date('Y-m-d H:i:s')));
            $coord->setCoorX($coord_xy->x);
            $coord->setCoorY($coord_xy->y);

            $userCoord->setUscoCoordinate($coord);
            $user = $token->getTokenUser();
            $userCoord->setUscoUser($user);

            $em->persist($userCoord);
            $em->flush();
        } catch (Exception $ex) {
            $logger->error('[TAVROS - ERROR]' . $ex);
            $apiResponse->setCode(120);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //GET POSITION
    public function getPositionAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $em = $this->container->get('Doctrine')->getManager();

        if (!$this->container->get('request')->getMethod() == 'POST') {
            $apiResponse->setCode(404);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $content = json_decode($this->container->get('request')->getContent());

        /* @var $token \Tavros\DomainBundle\Entity\Token */

        $token = $em->getRepository('TavrosDomainBundle:Token')->findOneByToken($content->_token);

        if (!$token) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $user = $token->getTokenUser();
            /* @var $coordinate \UserCoordinate */
            $coordinate = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findOneBy(
                    Array('uscoUser' => $user), Array('uscoId' => 'DESC'));
        } catch (Exception $ex) {
            $logger->error('[TAVROS - ERROR]' . $ex);
            $apiResponse->setCode(121);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($coordinate->getUscoCoordinate());
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

}
