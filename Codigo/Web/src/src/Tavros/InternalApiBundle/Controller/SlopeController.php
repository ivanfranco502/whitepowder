<?php

namespace Tavros\InternalApiBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;
use Tavros\InternalApiBundle\Entity\ApiResponse;
use Tavros\DomainBundle\Entity\Coordinate as Coordinate;
use Tavros\DomainBundle\Entity\SlopeCoordinate as SlopeCoordinate;
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

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
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

    //GET ONLY ALL SLOPE NAMES//
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

        $extData = $em->getRepository('TavrosDomainBundle:ExternalData')->findOneByExdaToken($content->_token);

        if (!$extData) {
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

            if ($coord[0]) {
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

    //RECOGNIZE A NEW SLOPE
    function recognitionAction() {
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
        /* @var $extData  \Tavros\DomainBundle\Entity\Token */
        if (!$extData) {
            $apiResponse->setCode(110);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $user = $extData->getExdaUser();

        /* @var $user  \Tavros\DomainBundle\Entity\Users */

        $roles = $user->getRoles();

        if (!in_array('ROLE_RECON', $roles)) {
            $apiResponse->setCode(117);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $slope = $em->getRepository('TavrosDomainBundle:Slope')->find($content->slope_id);

        /* @var $slope \Tavros\DomainBundle\Entity\Slope */

        if (!$slope) {
            $apiResponse->setCode(118);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        try {
            /* @var $actualCoord \SlopeCoordinate*/

            foreach ($slope->getCoordinates() as $actualCoord) {
                $slope->removeCoordinate($actualCoord);
                $actualCoord->setSlcoSlope(null);
            }
            $em->flush();
            foreach ($content->coordinates as $coord_xy) {
                $coord = new Coordinate();
                $slopeCoordinate = new SlopeCoordinate();

                $coord->setCoorCreatedDate(new \DateTime(date('Y-m-d H:i:s')));
                $coord->setCoorX($coord_xy->x);
                $coord->setCoorY($coord_xy->y);

                $slopeCoordinate->setSlcoCoordinate($coord);
                $slopeCoordinate->setSlcoSlope($slope);
                $slopeCoordinate->setSlcoUpdateDate(new \DateTime(date('Y-m-d H:i:s')));

                $slope->addCoordinate($slopeCoordinate);
            }
            $em->persist($slope);
            $em->flush();
        } catch (Exception $ex) {
            $logger->error('[TAVROS - ERROR]' . $ex);
            $apiResponse->setCode(119);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $apiResponse->setCode(200);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

    //GET SLOPE COORDINATE
    function allPathAction() {
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

        $slopes = $em->getRepository('TavrosDomainBundle:Slope')->findAll();

        if (!$slopes) {
            $apiResponse->setCode(111);
            $response->setContent($serializer->serialize($apiResponse, 'json'));
            return $response;
        }

        $minSlopes = Array();
        /* @var $slope \Tavros\DomainBundle\Entity\Slope */
        foreach ($slopes as $slope) {
            $minSlope = Array();
            $minSlope['slope_id'] = $slope->getSlopId();
            $minSlope['slope_description'] = $slope->getSlopDescription();
            $minSlope['slope_length'] = $slope->getSlopLength();
            $minSlope['slope_difficulty_color'] = $slope->getSlopDificulty()->getSldiColor()->getColoHexaCode();
            $minSlope['slope_difficulty_description'] = $slope->getSlopDificulty()->getSldiDescription();
//            $minSlope['slope_difficulty_color'] = '#008000';

            /* @var $coord \Tavros\DomainBundle\Entity\Coordinates */
            $minSlope['slope_coordinates'][] = Array();
            foreach ($slope->getCoordinates() as $coord) {
                /* @var $coord \Tavros\DomainBundle\Entity\Coordinates */
                $minCoordinates = Array();
                $minCoordinates['x'] = $coord->getSlcoCoordinate()->getCoorX();
                $minCoordinates['y'] = $coord->getSlcoCoordinate()->getCoorY();
                $minSlope['slope_coordinates'][] = $minCoordinates;
            }
            $minSlopes[] = $minSlope;
        }

        $apiResponse->setCode(200);
        $apiResponse->setPayload($minSlopes);
        $response->setContent($serializer->serialize($apiResponse, 'json'));
        return $response;
    }

}
