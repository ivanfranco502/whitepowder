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
        $em = $this->getContainer()->get('doctrine')->getManager();

        $allPositions = $em->getRepository('TavrosDomainBundle:UserCoordinate')->findAllLastPosition();

        foreach ($allPositions as $userPosition) {

            $today = new \DateTime('now');
            $userPositionDate = new \DateTime($userPosition['usco_update_date']);
            
            $interval = date_diff($today, $userPositionDate);

            $difference = $this->toMinutes($interval);

            $output->writeln('diference: ' . $difference);
            if ($difference > 30) {
                $usco = $em->getRepository('TavrosDomainBundle:UserCoordinate')->find($userPosition['usco_id']);
                $output->writeln('userArr: ' . $userPosition['usco_user_id']);
                $output->writeln('mode: ' . $usco->getUscoSkiMode());
                $output->writeln('user: ' . $usco->getUscoUser()->getId());
                $usco->setUscoSkiMode(0);
                $em->persist($usco);
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
