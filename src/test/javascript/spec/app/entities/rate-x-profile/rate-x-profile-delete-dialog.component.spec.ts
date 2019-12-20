import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { RateXProfileDeleteDialogComponent } from 'app/entities/rate-x-profile/rate-x-profile-delete-dialog.component';
import { RateXProfileService } from 'app/entities/rate-x-profile/rate-x-profile.service';

describe('Component Tests', () => {
  describe('RateXProfile Management Delete Component', () => {
    let comp: RateXProfileDeleteDialogComponent;
    let fixture: ComponentFixture<RateXProfileDeleteDialogComponent>;
    let service: RateXProfileService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [RateXProfileDeleteDialogComponent]
      })
        .overrideTemplate(RateXProfileDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RateXProfileDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RateXProfileService);
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
