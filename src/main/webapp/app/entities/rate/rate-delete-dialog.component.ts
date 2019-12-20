import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';

@Component({
  templateUrl: './rate-delete-dialog.component.html'
})
export class RateDeleteDialogComponent {
  rate: IRate;

  constructor(protected rateService: RateService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rateService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'rateListModification',
        content: 'Deleted an rate'
      });
      this.activeModal.dismiss(true);
    });
  }
}
