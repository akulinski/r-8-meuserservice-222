import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRateXProfile } from 'app/shared/model/rate-x-profile.model';
import { RateXProfileService } from './rate-x-profile.service';
import { RateXProfileDeleteDialogComponent } from './rate-x-profile-delete-dialog.component';

@Component({
  selector: 'jhi-rate-x-profile',
  templateUrl: './rate-x-profile.component.html'
})
export class RateXProfileComponent implements OnInit, OnDestroy {
  rateXProfiles: IRateXProfile[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected rateXProfileService: RateXProfileService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.rateXProfileService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IRateXProfile[]>) => (this.rateXProfiles = res.body));
      return;
    }
    this.rateXProfileService.query().subscribe((res: HttpResponse<IRateXProfile[]>) => {
      this.rateXProfiles = res.body;
      this.currentSearch = '';
    });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInRateXProfiles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRateXProfile) {
    return item.id;
  }

  registerChangeInRateXProfiles() {
    this.eventSubscriber = this.eventManager.subscribe('rateXProfileListModification', () => this.loadAll());
  }

  delete(rateXProfile: IRateXProfile) {
    const modalRef = this.modalService.open(RateXProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rateXProfile = rateXProfile;
  }
}
