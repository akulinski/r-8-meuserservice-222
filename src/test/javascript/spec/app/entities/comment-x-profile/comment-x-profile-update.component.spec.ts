import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { CommentXProfileUpdateComponent } from 'app/entities/comment-x-profile/comment-x-profile-update.component';
import { CommentXProfileService } from 'app/entities/comment-x-profile/comment-x-profile.service';
import { CommentXProfile } from 'app/shared/model/comment-x-profile.model';

describe('Component Tests', () => {
  describe('CommentXProfile Management Update Component', () => {
    let comp: CommentXProfileUpdateComponent;
    let fixture: ComponentFixture<CommentXProfileUpdateComponent>;
    let service: CommentXProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [CommentXProfileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CommentXProfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentXProfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CommentXProfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CommentXProfile(123);
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
        const entity = new CommentXProfile();
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
