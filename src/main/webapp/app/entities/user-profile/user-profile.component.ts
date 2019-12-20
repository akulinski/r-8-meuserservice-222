import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from './user-profile.service';
import { UserProfileDeleteDialogComponent } from './user-profile-delete-dialog.component';

@Component({
  selector: 'jhi-user-profile',
  templateUrl: './user-profile.component.html'
})
export class UserProfileComponent implements OnInit, OnDestroy {
  userProfiles: IUserProfile[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected userProfileService: UserProfileService,
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
      this.userProfileService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IUserProfile[]>) => (this.userProfiles = res.body));
      return;
    }
    this.userProfileService.query().subscribe((res: HttpResponse<IUserProfile[]>) => {
      this.userProfiles = res.body;
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
    this.registerChangeInUserProfiles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUserProfile) {
    return item.id;
  }

  registerChangeInUserProfiles() {
    this.eventSubscriber = this.eventManager.subscribe('userProfileListModification', () => this.loadAll());
  }

  delete(userProfile: IUserProfile) {
    const modalRef = this.modalService.open(UserProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userProfile = userProfile;
  }
}
