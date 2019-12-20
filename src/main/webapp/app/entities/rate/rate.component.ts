import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRate } from 'app/shared/model/rate.model';
import { RateService } from './rate.service';
import { RateDeleteDialogComponent } from './rate-delete-dialog.component';

@Component({
  selector: 'jhi-rate',
  templateUrl: './rate.component.html'
})
export class RateComponent implements OnInit, OnDestroy {
  rates: IRate[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected rateService: RateService,
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
      this.rateService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IRate[]>) => (this.rates = res.body));
      return;
    }
    this.rateService.query().subscribe((res: HttpResponse<IRate[]>) => {
      this.rates = res.body;
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
    this.registerChangeInRates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRate) {
    return item.id;
  }

  registerChangeInRates() {
    this.eventSubscriber = this.eventManager.subscribe('rateListModification', () => this.loadAll());
  }

  delete(rate: IRate) {
    const modalRef = this.modalService.open(RateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rate = rate;
  }
}
