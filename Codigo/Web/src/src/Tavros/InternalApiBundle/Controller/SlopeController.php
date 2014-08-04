<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\AnonymousToken;
use \FOS\UserBundle\Entity\User as User;

class SlopeController extends Controller {

    //GET ALL SLOPES WITH INFO//
    function allAction() {
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

        $token = $em->getRepository('TavrosDomainBundle:Token')->findOneByToken($content->_token);

        if (!$token) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $slopes = $em->getRepository('TavrosDomainBundle:Slope')->findAll();

        if (!$slopes) {
            $apiResponse->setCode(111);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($slopes);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    function allNamesAction() {
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

        $token = $em->getRepository('TavrosDomainBundle:Token')->findOneByToken($content->_token);

        if (!$token) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $slopes = $em->getRepository('TavrosDomainBundle:Slope')->findAll();

        if (!$slopes) {
            $apiResponse->setCode(115);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $payload = Array();
        foreach ($slopes as $slope) {
            /* @var $slope \Tavros\DomainBundle\Entity\Slope */
            $s = Array();
            $s['slope_id'] = $slope->getSlopId();
            $s['slope_description'] = $slope->getSlopDescription();

            $coord = $slope->getCoordinates();
            if ($coord) {
                $s['slope_recognized'] = 1;
            } else {
                $s['slope_recognized'] = 0;
            }

            $payload[] = $s;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($payload);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

}
