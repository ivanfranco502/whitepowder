<?php

namespace Tavros\WebBundle\Admin;

use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Form\FormMapper;

class GeneralInformationAdmin extends Admin {

    protected $baseRouteName = 'generalinformation';
    protected $baseRoutePattern = 'generalinformation';

    // Fields to be shown on create/edit forms
    protected function configureFormFields(FormMapper $formMapper) {
        $formMapper
                ->add('geinId')
                ->add('geinAmenities')
                ->add('geinMaximumHeight')
                ->add('geinMinimumHeight')
                ->add('geinSeasonSince')
                ->add('geinSeasonTil');
    }

    // Fields to be shown on filter forms
    protected function configureDatagridFilters(DatagridMapper $datagridMapper) {
        $datagridMapper
                ->add('geinId')
                ->add('geinAmenities')
                ->add('geinMaximumHeight')
                ->add('geinMinimumHeight')
                ->add('geinSeasonSince')
                ->add('geinSeasonTil');
    }

    // Fields to be shown on lists
    protected function configureListFields(ListMapper $listMapper) {
        $listMapper
                ->add('geinId')
                ->add('geinAmenities')
                ->add('geinMaximumHeight')
                ->add('geinMinimumHeight')
                ->add('geinSeasonSince')
                ->add('geinSeasonTil');
    }

}
