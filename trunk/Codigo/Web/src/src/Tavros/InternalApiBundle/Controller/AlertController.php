<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;

class AlertController extends Controller {

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
        $receivedAlert = new Tavros\DomainBundle\Entity\Alert();

        $receivedAlert->setAlerUser($extData->getExdaUser());
        $receivedAlert->setAlerXPosition($content->X);
        $receivedAlert->setAlerYPosition($content->Y);
        $receivedAlert->setAlerRead(0);
        $receivedAlert->setAlerDate(new \DateTime(date('Y-m-d H:i:s')));

        $em->persist($receivedAlert);
        $em->flush();

        $apiResponse->setCode(200);
        $apiResponse->setPayload('');
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

}
