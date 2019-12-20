import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { FollowerXFollowedDeleteDialogComponent } from 'app/entities/follower-x-followed/follower-x-followed-delete-dialog.component';
import { FollowerXFollowedService } from 'app/entities/follower-x-followed/follower-x-followed.service';

describe('Component Tests', () => {
  describe('FollowerXFollowed Management Delete Component', () => {
    let comp: FollowerXFollowedDeleteDialogComponent;
    let fixture: ComponentFixture<FollowerXFollowedDeleteDialogComponent>;
    let service: FollowerXFollowedService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [FollowerXFollowedDeleteDialogComponent]
      })
        .overrideTemplate(FollowerXFollowedDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FollowerXFollowedDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FollowerXFollowedService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
