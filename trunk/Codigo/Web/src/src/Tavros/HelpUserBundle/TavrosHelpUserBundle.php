<?php

namespace Tavros\HelpUserBundle;

use Symfony\Component\HttpKernel\Bundle\Bundle;

class TavrosHelpUserBundle extends Bundle {

    public function getParent() {
        return 'FOSUserBundle';
    }

}
