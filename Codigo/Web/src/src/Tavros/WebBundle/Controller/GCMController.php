<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class GCMController extends Controller {

    public function sendNotificationAction() {
        // include apiKey
        $apiKey = $this->container->getParameter('api_key');
        // lucas
        $registration_ids = array("APA91bEx-tzPcQj-bfl3TF9t9FqPCOIlK8ad6TWBnm0i49ZFnjyXMrv_4nF5fqGpHA_mMwQMyN04uji7PcKZUbmdqBoaEIXsrmICsAxQZ5XiIEqRX-Sv_aIx3l3BYnY-OU6jhu6o1837LqavDEHD9gDMWyn0TB_qxOFHUapoXHGq4HJiDkG3XaM");
        //alexa
//        $registration_ids = array("APA91bHxstSG9Xk-kBcARfJfj77gvqcwGy3EwhjoqNEdSzE-LaGfJzsBQdYPRSXpBII7YD8mZiFRgdjsdFMHtHoHBcpntzMK0FTtFK4Esu2T8fh3jh_k9u1sGDRUz74pfuHcYgNFXcOE1EWXgRFeUy9mY-33al_asi6qszNLc8sBg-BD_Y7S3gM");

        $message = array("id" => "1",
            "title" => "Avalancha",
            "body" => "Se pudrió todo");

        // Set POST variables
        $url = 'https://android.googleapis.com/gcm/send';

        $fields = array(
            'registration_ids' => $registration_ids,
            'data' => $message
        );

        $headers = array(
            'Authorization: key=' . $apiKey,
            'Content-Type: application/json'
        );
        // Open connection
        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);

        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Disabling SSL Certificate support temporarly
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        // Execute post
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);
        echo $result;

        return $this->render('TavrosWebBundle:Administrator:center.html.twig', array('coordinate_x' => 10,
                    'coordinate_y' => 50));
    }

}
