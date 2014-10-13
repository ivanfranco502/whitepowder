<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;

class AlertController extends Controller {

    //SEND ALERT TO SKI CENTER    
    public function sendAction() {
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

        /* @var $receivedAlert \Tavros\DomainBundle\Entity\Alert */
        $receivedAlert = new \Tavros\DomainBundle\Entity\Alert;

        $user = $extData->getExdaUser();
        $receivedAlert->setAlerUser($user);

        $lastPosition = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findLastPosition($user->getId());
        
        /* @var $lastPositionObject \Tavros\DomainBundle\Entity\UserCoordinate */
        $lastPositionObject = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findOneBy(Array('uscoId' => $lastPosition[0]['usco_id']));
        
        $lastPositionObject->getUscoCoordinate()->setCoorX($content->coordinate->x);
        $lastPositionObject->getUscoCoordinate()->setCoorY($content->coordinate->y);
        $lastPositionObject->setUscoSkiMode(1);

        $receivedAlert->setAlerXPosition($content->coordinate->x);
        $receivedAlert->setAlerYPosition($content->coordinate->y);
        $receivedAlert->setAlerRead(0);
        $receivedAlert->setAlerDate(new \DateTime(date('Y-m-d H:i:s')));

        $em->persist($receivedAlert);
        
        $lastPositionObject->setUscoAlert($receivedAlert);
        $em->persist($lastPositionObject);
        $em->flush();

        $apiResponse->setCode(200);
        $apiResponse->setPayload('');
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //GET ALL ALERTS
    public function allAction() {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $em = $this->container->get('Doctrine')->getManager();
        //TODO VERIFICAR QUE SEA ADMINISITRADOR

        $alerts = $em->getRepository('TavrosDomainBundle:Alert')->findBy(Array('alerRead' => '0'), Array('alerDate' => 'ASC'));
        $count = count($alerts);
        $apiResponse->setCode(200);

        $content = Array();
        $content['total'] = $count;
        $content['last'] = array_slice($alerts, 0, 5);
        $apiResponse->setPayload($content);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //READ ID ALERTS
    public function readAction($id) {
        $logger = $this->container->get('logger');
        $serializer = $this->container->get('jms_serializer');
        $apiResponse = new ApiResponse();
        $response = new Response();
        $response->headers->set('Content-Type', 'application/json');
        $em = $this->container->get('Doctrine')->getManager();
        //TODO VERIFICAR QUE SEA ADMINISITRADOR

        $alert = $em->getRepository('TavrosDomainBundle:Alert')->find($id);
        $alert->setAlerRead(1);
        $em->persist($alert);
        $em->flush();

        $apiResponse->setCode(200);
        $apiResponse->setPayload('');
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

}
