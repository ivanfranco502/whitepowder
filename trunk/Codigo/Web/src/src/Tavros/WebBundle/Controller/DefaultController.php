<?php

namespace Tavros\WebBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller {

    public function indexAction($name) {
        return $this->render('TavrosWebBundle:Default:index.html.twig', array('name' => $name));
    }

    public function loginAction() {
        //SHOWS LOGIN FORM//    
        return $this->render('TavrosWebBundle:Default:login.html.twig');
    }

    public function loginCheckAction() {
        //VALIDATE USER//

        $url = 'http://whitetavros.com/Sandbox/web/internalApi/user/login';
        $jsonResponse = $this->curl_post($url, array(
            'username' => $_POST['username'],
            'password' => $_POST['password'],
            'origin' => 'web'));

        $response = json_decode($jsonResponse);

        if ($response->code === 200) {
            return new \Symfony\Component\HttpFoundation\RedirectResponse('tavros_administrator_home');
        } else {
            return $this->render('TavrosWebBundle:Default:login.html.twig');
        }
    }

    function curl_post($url, array $post = NULL, array $options = array()) {
        $defaults = array(
            CURLOPT_POST => 1,
            CURLOPT_HEADER => 0,
            CURLOPT_URL => $url,
            CURLOPT_FRESH_CONNECT => 1,
            CURLOPT_RETURNTRANSFER => 1,
            CURLOPT_FORBID_REUSE => 1,
            CURLOPT_TIMEOUT => 4,
            CURLOPT_POSTFIELDS => http_build_query($post)
        );

        $ch = curl_init();
        curl_setopt_array($ch, ($options + $defaults));
        if (!$result = curl_exec($ch)) {
            trigger_error(curl_error($ch));
        }
        curl_close($ch);
        return $result;
    }

}
