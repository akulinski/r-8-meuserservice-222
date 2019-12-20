import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { RateXProfileComponent } from 'app/entities/rate-x-profile/rate-x-profile.component';
import { RateXProfileService } from 'app/entities/rate-x-profile/rate-x-profile.service';
import { RateXProfile } from 'app/shared/model/rate-x-profile.model';

describe('Component Tests', () => {
  describe('RateXProfile Management Component', () => {
    let comp: RateXProfileComponent;
    let fixture: ComponentFixture<RateXProfileComponent>;
    let service: RateXProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [RateXProfileComponent],
        providers: []
      })
        .overrideTemplate(RateXProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RateXProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RateXProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new RateXProfile(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.rateXProfiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
