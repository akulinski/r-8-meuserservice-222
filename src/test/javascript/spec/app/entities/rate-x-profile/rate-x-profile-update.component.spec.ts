import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { RateXProfileUpdateComponent } from 'app/entities/rate-x-profile/rate-x-profile-update.component';
import { RateXProfileService } from 'app/entities/rate-x-profile/rate-x-profile.service';
import { RateXProfile } from 'app/shared/model/rate-x-profile.model';

describe('Component Tests', () => {
  describe('RateXProfile Management Update Component', () => {
    let comp: RateXProfileUpdateComponent;
    let fixture: ComponentFixture<RateXProfileUpdateComponent>;
    let service: RateXProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [RateXProfileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RateXProfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RateXProfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RateXProfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RateXProfile(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new RateXProfile();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
