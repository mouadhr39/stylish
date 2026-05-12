/***
 * Dialog show/hide configuration format.
 *
 * The configuration is a JSON array of objects. Each object controls one
 * trigger-based behavior inside the dialog.
 *
 * Properties:
 * - container: CSS selector for the parent container that holds the trigger and targets.
 * - trigger: CSS selector for the input/select element whose value is watched.
 * - value: The trigger value that activates the configured action.
 * - action: "only-hide" or "show-hide".
 *
 * only-hide:
 *   Hides or shows a single target container depending on the trigger value.
 *   Required: target
 *
 * show-hide:
 *   Shows one container and hides another when the trigger value matches.
 *   Required: targetToShow, targetToHide
 *
 * Examples:
 *   only-hide:
 *   {"showHideConfigs":[{
 *     "container": ".show-hide-container",
 *     "trigger": ".card-type",
 *     "value": "opt-one",
 *     "action": "only-hide",
 *     "target": ".second-card-group"
 *   }]}
 *
 *   show-hide:
 *   {"showHideConfigs":[{
 *     "container": ".show-hide-container",
 *     "trigger": ".card-type",
 *     "value": "opt-one",
 *     "action": "show-hide",
 *     "targetToShow": ".second-card-group",
 *     "targetToHide": ".first-card-group"
 *   }]}
 */

(function (document, $) {
  "use strict";

  const DialogShowHideManager = (self = {
    SHOW_HIDE_ACTION: "show-hide",
    ONLY_HIDE_ACTION: "only-hide",

    isString: (value) => {
      return (
        value !== undefined &&
        (typeof value === "string" || value instanceof String)
      );
    },

    validateTarget: (config) => {
      if (config.action === "only-hide") {
        return self.isString(config.target);
      } else if (config.action === "show-hide") {
        return (
          self.isString(config.targetToShow) &&
          self.isString(config.targetToHide)
        );
      } else {
        console.warn(
          `Wrong action target in Dialog show-hide configuration, see documentation for supported actions and their required parameters`,
        );
        return false;
      }
    },

    validateConfigAction: (config) => {
      return (
        self.isString(config.action) &&
        config.action.length > 0 &&
        (config.action === self.ONLY_HIDE_ACTION ||
          config.action === self.SHOW_HIDE_ACTION)
      );
    },

    validateConfig: (config) => {
      return (
        self.isString(config.container) &&
        config.container.length > 0 &&
        config.container.startsWith(".") &&
        self.isString(config.trigger) &&
        config.trigger.length > 0 &&
        config.trigger.startsWith(".") &&
        self.isString(config.value) &&
        config.value.length > 0 &&
        self.validateConfigAction(config) &&
        self.validateTarget(config)
      );
    },

    onlyHideAction: (config, element) => {
      if ($(element).find(config.trigger).val() === config.value) {
        $(element).find(config.target).hide();
      } else {
        $(element).find(config.target).show();
      }
    },

    showHideAction: (config, element) => {
      if ($(element).find(config.trigger).val() === config.value) {
        $(element).find(config.targetToShow).show();
        $(element).find(config.targetToHide).hide();
      } else {
        $(element).find(config.targetToShow).hide();
        $(element).find(config.targetToHide).show();
      }
    },

    apply: (config) => {
      $(config.container).each((idx, element) => {
        if (config.action === self.SHOW_HIDE_ACTION) {
          self.showHideAction(config, element);
        } else if (config.action === self.ONLY_HIDE_ACTION) {
          self.onlyHideAction(config, element);
        } else {
          console.warn(
            `Unsupported action ${config.action} in Dialog show-hide configuration`,
          );
        }

        // attach change event scoped to this container for multifield support
        $(element)
          .find(config.trigger)
          .off("change.showHide")
          .on("change.showHide", (e) => {
            self.apply(config);
          });
      });
    },
  });

  // when dialog gets injected
  $(document).on("foundation-contentloaded", (e) => {
    applyConfigurations();
  });

  // for multifield support: reapply when items are added or removed
  $(document).on("coral-collection:add coral-collection:remove", (e) => {
    // small delay to ensure DOM is updated
    setTimeout(applyConfigurations, 100);
  });

  function applyConfigurations() {
    const configString = $(".show-hide-configuration").val();

    if (configString !== undefined) {
      const parsed = JSON.parse(configString);
      const configs = Array.isArray(parsed)
        ? parsed
        : parsed.showHideConfigs || [];

      for (const config of configs) {
        if (DialogShowHideManager.validateConfig(config)) {
          DialogShowHideManager.apply(config);
        }
      }
    }
  }
})(document, Granite.$);
