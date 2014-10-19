<?php

namespace Tavros\WebBundle\Command;

use Symfony\Bundle\FrameworkBundle\Command\ContainerAwareCommand;
use Symfony\Component\Console\Input\InputArgument;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Input\InputOption;
use Symfony\Component\Console\Output\OutputInterface;

class ClearMapCommand extends ContainerAwareCommand {

    protected function configure() {
        $this
                ->setName('tavros:web:clear')
                ->setDescription('Clear all old Markers from the map.');
    }

    protected function execute(InputInterface $input, OutputInterface $output) {
        date_default_timezone_set('America/Argentina/Buenos_Aires');
        $em = $this->getContainer()->get('doctrine')->getManager();

        $allPositions = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findAll();

        foreach ($allPositions as $userPosition) {

            $today = new \DateTime('now');
            $userPositionDate = $userPosition->getUscoUpdateDate();

            $interval = date_diff($today, $userPositionDate);

            $difference = $this->toMinutes($interval);

            $output->writeln('diference: ' . $difference);
            if (intval($difference) > 30) {
//                $usco = $em->getRepository('TavrosDomainBundle:UserCoordinate')->find($userPosition['usco_id']);
//                $output->writeln('userArr: ' . $userPosition['usco_user_id']);
//                $output->writeln('mode: ' . $usco->getUscoSkiMode());
//                $output->writeln('user: ' . $usco->getUscoUser()->getId());
                $userPosition->setUscoSkiMode(0);

                if ($userPosition->getUscoAlert()) {
                    $userPosition->getUscoAlert()->setAlerRead(1);
                }

                $userPosition->setUscoAlert();


//                if ($userPosition->getUscoAlert() !== NULL) {
//                    $alert = $em->getRepository('TavrosDomainBundle:Alert')->find($userPosition->getUscoAlert());
//                    $alert->setAlerRead(1);
//                    $userPosition->setUscoAlert(NULL);
//                }

//                $em->persist($alert);
                $em->persist($userPosition);
                $em->flush();
            }
        }

        $output->writeln('success');
    }

    private function toMinutes($interval) {

        $minutes = $interval->format('%i');
        $hours = $interval->format('%h');
        $days = $interval->format('%d');

        return $minutes + $hours * 60 + $days * 24 * 60;
    }

}
