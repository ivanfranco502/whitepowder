<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Core\Authentication\Token\AnonymousToken;
use \FOS\UserBundle\Entity\User as User;

class DefaultController extends Controller {

    public function basicInformationAction() {

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

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $generalInformationDTO = $em->getRepository('TavrosDomainBundle:GeneralInformationDTO')->findAll();

        if (!$generalInformationDTO) {
            $apiResponse->setCode(116);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($generalInformationDTO[0]);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    public function numberVersionAction() {

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

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $version = $em->getRepository('TavrosDomainBundle:Version')->findActualVersion();

        if (!$version) {
            $apiResponse->setCode(116);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($version[0]);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    public function allAlertsAction() {
//        $serializer = $this->container->get('jms_serializer');
//        $apiResponse = new ApiResponse();
//        $response = new Response();
//        $response->headers->set('Content-Type', 'application/json');
//        $em = $this->container->get('Doctrine')->getManager();
//        
//
//        $apiResponse->setCode(200);
//        $apiResponse->setPayload();
//        $response->setContent($serializer->serialize($apiResponse, 'json'));
//        return $response;
    }

}
