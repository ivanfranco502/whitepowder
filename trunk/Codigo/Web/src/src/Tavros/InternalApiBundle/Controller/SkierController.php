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

    //SET CURRENT POSITION FOR ONE SKIER
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

        /* @var $extData \Tavros\DomainBundle\Entity\ExternalData */

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $coord_xy = $content->coordinate;
            $coord = new Coordinate();
            $userCoord = new UserCoordinate();
            /* @var $userCoord \Tavros\DomainBundle\Entity\UserCoordinate */
            $coord->setCoorCreatedDate(new \DateTime(date('Y-m-d H:i:s')));
            $coord->setCoorX($coord_xy->x);
            $coord->setCoorY($coord_xy->y);

            $userCoord->setUscoCoordinate($coord);
            $userCoord->setUscoSkiMode(1);
            $user = $extData->getExdaUser();
            $userCoord->setUscoUser($user);
            $userCoord->setUscoUpdateDate(new \DateTime(date('Y-m-d H:i:s')));

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

    //GET CURRENT POSITION FOR ONE SKIER
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
        $token = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByToken($content->_token);

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

    //GET ALL SKIER
    public function getAllAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $em = $this->container->get('Doctrine')->getManager();
        //TODO VERIFICAR QUE SEA ADMINISITRADOR
        $allPositions = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findAllLastPosition();

        /* @var $userPosition \UserCoordinate */
        $skiersDTO = Array();
        foreach ($allPositions as $userPosition) {
            $userPositionObject = $em->getRepository('TavrosDomainBundle:UserCoordinate')->find($userPosition['usco_id']);
            $skierDTO = Array();
            $skierDTO['id'] = $userPosition['usco_id'];
            $skierDTO['username'] = $userPositionObject->getUscoUser()->getUsername();

            $positionDTO = Array();
            $positionDTO['coor_X'] = $userPositionObject->getUscoCoordinate()->getCoorX();
            $positionDTO['coor_Y'] = $userPositionObject->getUscoCoordinate()->getCoorY();
            $skierDTO['position'] = $positionDTO;

            $skiersDTO[] = $skierDTO;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($skiersDTO);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //SET GCM REGISTRATION CODE FOR SKIER ALERTS
    public function setGCMRegistrationCodeAction() {
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

        /* @var $extData \Tavros\DomainBundle\Entity\ExternalData */

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $registrationCode = $content->registrationCode;

            $extData->setExdaRegistrationCode($registrationCode);
            $em->persist($extData);
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

    //SET GCM REGISTRATION CODE FOR SKIER ALERTS
    public function offSkiModeAction() {
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

        /* @var $extData \Tavros\DomainBundle\Entity\ExternalData */

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            $lastPosition = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findLastPosition($extData->getExdaUser());

            $lastUserCoordinate = $em->getRepository('TavrosDomainBundle:UserCoordinate')->find($lastPosition["usco_id"]);

            $lastUserCoordinate->setSkiMode(0);
            $em->persist($lastUserCoordinate);
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

}
